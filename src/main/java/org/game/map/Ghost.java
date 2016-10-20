package org.game.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List; 

import org.game.map.Map;
import org.game.GraphicLooper;
import org.game.Game;
import org.game.resource.ResourceManager;
import org.game.resource.SpriteImageResource;
import org.game.geom.Polygon; 
import org.game.geom.BresenhamLine;
import org.game.math.Point2D;
import org.game.math.Vector2D;
import org.game.util.GameUtil;

public class Ghost extends MapObject {
    
    private Vector2D mDir = new Vector2D();
    private Point2D mPos = new Point2D(50, 50); 
    private Vector2D mVel = new Vector2D(0, 0);
    private int mSpeed = 3;
    
    public Ghost(int x, int y) {
        mPos.set(x, y);
    }
    
    public Point2D getPosition() {
        return mPos;
    }
    
    public double getAngle() {
        return mDir.sub(getPosition()).angle();
    }
    
    public double getDistanceToPlayer() {
        return new Vector2D(mPos).sub(getMap().getPlayer().getPosition()).length();
    }
    
    /**
     * 현재 플레이어의 이미지를 가져옵니다.
     * 
     * 걷고, 서있고... 등등?
     * 
     * @return 프레임을 반환합니다.
     */
    private SpriteImageResource.SpriteImage.Frame getCurrentSpriteFrame(long d) {
        String k = String.join(".", "player", "walk", GameUtil.getDirectionByRadian(getAngle()));
        SpriteImageResource r = ResourceManager.getInstance().getSprite(k);
        
        SpriteImageResource.SpriteImage.Frame f = r.getFrame(2); // 기본 상태
        
        if (mVel.getX() != 0 || mVel.getY() != 0) { // 움직임이 발생하면
            f = r.getCurrentFrame(d); // 델타값을 넣어 현재 프레임을 뽑아옴
        }
        
        // TODO ... 걷고, 서있는것 이외에 무언가 처리해야할게 있는가???
        
        return f;
    }
    
    @Override
    public void draw(Game g, Graphics2D g2d) { 
        SpriteImageResource.SpriteImage.Frame f = getCurrentSpriteFrame(g.getDelta());
        Point2D p = getPosition();
        
        g2d.drawImage(f.getImage(), 
                      p.getX() - f.getWidth() / 2, 
                      p.getY() - f.getHeight() / 2, 
                      null);
    }
    
    @Override
    public void update(Game c) {
        double d = getDistanceToPlayer();
        boolean isNearPlayer = d < 200;
        boolean isLightProjected = false;
        
        mVel.setX(0);
        mVel.setY(0);
        
        if (isNearPlayer) { // 플레이어가 근처에 있는경우
            mVel.setX(mSpeed);
            mVel.setY(mSpeed); 
        }
        
        if (isLightProjected || (isNearPlayer && getMap().getPlayer().isTurnOnLight())) { // 플레이어가 근처에있으면서 플래시가 켜진 경우
            mVel.setX(mSpeed * 2);
            mVel.setY(mSpeed * 2); 
        }
        
        if (mVel.getX() != 0 && mVel.getY() != 0) {
            
            mDir.set(getMap().getPlayer().getPosition());
            
            List<Point2D> l = getMap().getPath(mPos, getMap().getPlayer().getPosition());
            
            if ( ! l.isEmpty()) {
                
                double n = new Vector2D(l.get(0)).sub(mPos).angle();
                int x = (int) (mPos.getX() + mVel.getX() * Math.cos(n));
                int y = (int) (mPos.getY() + mVel.getY() * Math.sin(n));
                
                mPos.setX(x);
                mPos.setY(y);
            }
        }
    }
}
