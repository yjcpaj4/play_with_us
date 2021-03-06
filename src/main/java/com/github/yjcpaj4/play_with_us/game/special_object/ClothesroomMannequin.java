package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.object.Player;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.game.Map;
import com.github.yjcpaj4.play_with_us.game.object.Portal;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.math.Matrix2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.math.Vector2D;
import com.github.yjcpaj4.play_with_us.resource.SoundResource;
import com.github.yjcpaj4.play_with_us.resource.SpriteResource;
import com.github.yjcpaj4.play_with_us.util.MathUtil;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ClothesroomMannequin extends GameObject {
    
    private static final int X = 44;
    private static final int Y = 368;
    
    private static final int SPEED = 10;
    private static final String YES = "살펴본다.";
    private static final String NO = "그만둔다.";
    
    private boolean mSuprise = false;
    private Vector2D mVel = new Vector2D();
    private Polygon mCollider = new Circle(X, Y, 10);
    
    private long mAnimDuration = 0;
    private long mSpriteDuration = 0;
    
    private boolean mLookingAtMe = false;
    
    private boolean mPlayFeetSound = false;
    
    public void setSuprise() {
        mSuprise = true;
    }
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    /**
     * 플레이어와 마네킹 사이 각도
     * 
     * @param p
     * @return 
     */
    private double getAngleWithPlayer(Player p) {
        return new Vector2D(p.getPosition()).subtract(mCollider.getPosition()).toAngle();
    }
    
    private void playFeetSound() {
        if ( ! mPlayFeetSound) {
            Application.getInstance().getResource().getSound("snd.obj.clothesroom.mannequin.feet").play(-1);
            mPlayFeetSound = true;
        }
    }
    
    private SpriteResource.Frame getCurrentSpriteFrame(GameLayer g, long d) {
        boolean isIdle = mVel.getX() == 0 && mVel.getY() == 0;
        
        List<String> l = new ArrayList<>(4);
        l.add("sprt");
        l.add("clothesroom");
        l.add("mannequin");
        
        if (isIdle) {
            l.add("idle");   
            l.add("s");
        } else {
            l.add("walk");
            l.add(MathUtil.getSimpleDirectionByRadian(getAngleWithPlayer(g.getPlayer())));
        }
        
        SpriteResource r = g.getResource().getSprite(String.join(".", l));
        int n = (int) (mSpriteDuration / r.getFPS() % r.getLength());
        
        mSpriteDuration += d;
        
        return r.getFrame(n);
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if ( ! mSuprise) {
            
            if (new Vector2D(getPosition()).subtract(g.getPlayer().getPosition()).length() <= 40
            && g.getInput().isKeyOnce(KeyEvent.VK_F)) {

                InterativeLayer l = new InterativeLayer(Application.getInstance()) {

                    @Override
                    protected void pause() {
                        super.pause();

                        if (getCurrentAnswer().equals(YES)) {
                            g.showMessage("아무것도 발견하지 못하였습니다.", 1000);
                        }
                    }
                };
                l.setQuestion("살펴 보시겠습니까?");
                l.setAnswers(new String[] { YES, NO });
                l.setBackground(g.getResource().getImage("img.bg.clothesroom.mannequin"));
                g.showLayer(l);
            }
            return;
        }

        final Player o = g.getPlayer();
        
        // 0.5초후 마네킨방향으로 돌리게함.
        if (mAnimDuration > 500) {
            if ( ! mLookingAtMe) {
                o.getDirection().set(getPosition());
                mLookingAtMe = true;
            } 
        
            // 1.3초후 마네킨이 따라옴.
            if (mAnimDuration > 1300) {
                playFeetSound();
                
                mVel.set(SPEED, SPEED);

                Point2D p1 = mCollider.getPosition();
                Point2D p2 = o.getPosition();

                List<Point2D> l = g.getMap().getPath(p1, p2);
                if ( ! l.isEmpty()) {
                    double n = new Vector2D(l.get(0)).subtract(p1).toAngle();
                    double tx = SPEED * Math.cos(n);
                    double ty = SPEED * Math.sin(n);

                    mCollider.transform(Matrix2D.translate(tx, ty));
                }
                
                Vector2D v = new Vector2D(p1).subtract(p2);

                if (v.length() <= 170) {
                    Portal p = getMap().getPortalByDestMap("map.livingroom");
                    List<Point2D> pl = getMap().getPath(p.getPosition(), o.getPosition());
                    
                    double n = new Vector2D(pl.get(0)).subtract(o.getPosition()).toAngle();
                    float tx = (float) (5 * Math.cos(n));
                    float ty = (float) (5 * Math.sin(n));
                    
                    o.getVelocity().set(tx, ty);
                }
                
                if (v.length() <= 70) {
                    Map m = getMap();
                    Portal p = m.getPortalByDestMap("map.livingroom");
                    p.enterPortal(o);

                    o.setInputEnable();
                    
                    m.removeObject(this);
                    m.removeObject(m.getFirstObjectByClass(ClothesroomMannequinMine.class));
                    
                    g.getResource().getSound("snd.player.breath").play();
                    g.getResource().getSound("snd.obj.clothesroom.mannequin.feet").stop();
                    return;
                }
            }
        }
        
        mAnimDuration += delta;
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        SpriteResource.Frame f = getCurrentSpriteFrame(g, delta);
        Point2D p = mCollider.getPosition();
        
        int x = (int) (p.getX() - f.getWidth() / 2);
        int y = (int) (p.getY() - f.getHeight() + ((Circle) mCollider).getRadius());
        
        g2d.drawImage(f.getImage(), x, y, null);
    }
}
