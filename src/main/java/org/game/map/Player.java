package org.game.map;
 
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; 
import javafx.scene.paint.RadialGradient;
import javax.imageio.ImageIO;
 
import org.game.map.Map;
import org.game.GameLoop;
import org.game.DrawableObject;
import org.game.Game;
import org.game.Main;
import org.game.SpriteManager;
import org.game.geom.Circle;
import org.game.geom.EarCutTriangulator;
import org.game.geom.Polygon;
import static org.game.map.Map.MAP_HEIGHT;
import static org.game.map.Map.MAP_WIDTH; 
import org.game.geom.BresenhamLine;
import org.game.geom.Raycast;
import org.game.geom.SAT;
import org.game.math.Point2D;
import org.game.math.Vector2D;
 
public class Player extends Circle implements DrawableObject {
    
    private static final boolean DEBUG = true;
    
    private Map map;
 
    private boolean isTurnOnFlash = true;
     
    private Vector2D dir = new Vector2D(0, 0);    
    private Vector2D vel = new Vector2D();    
    
    
    private SpriteManager sp = new SpriteManager();
    
    public Player(Map m) {
        super(620, 700, 42);
        this.map = m;
        
        sp.loadSprite("player");
    }
    
    public void setMap(Map m) {
        this.map = m;
    }
    
    public boolean isTurnOnFlash() {
        return isTurnOnFlash;
    }
    
    public void setTurnOffFlash() {
        this.isTurnOnFlash = false;
    }
    
    public void setTurnOnFlash() {
        this.isTurnOnFlash = true;
    }
    
    public void toggleFlash() {
        this.isTurnOnFlash = ! this.isTurnOnFlash;
    }
    
    private Polygon projectLight(Point2D pos, double ang) { 
        if ( ! isTurnOnFlash) {
            return null;
        }
        
        List<Point2D> l = new ArrayList<>();
        l.add(pos);
        
        for (Point2D e : Raycast.getRaycast(pos, ang, map.getWall2())) {  
            l.add(e);    
        }
        
        return new Polygon(l); 
    }
    
    /**
     * 캐릭터가 가리키는 방향.
     * 
     * @return Vector2D 클래스로 반환됩니다. 
     */
    public Vector2D getDirection() {
        return dir;
    }
    
    /**
     * 캐릭터의 가속도.
     * 
     * @return 
     */
    public Vector2D getVelocity() {
        return vel;
    }     
    
    // Math.atan2(dir - pos) = 각도
    public double getAngle() {
        return dir.sub(getPosition()).getAngle();
    }
    
    private java.awt.Polygon pr(Point2D pos, double angle) {
        Polygon p = projectLight(pos, angle);
        
        return new java.awt.Polygon(p.getXPoints(), p.getYPoints(), p.getPoints().size());
    }
    
    @Override
    public void draw(GameLoop c, Graphics2D g2d) { 
        
        Point2D p = getPosition();
        float x = p.getX();
        float y = p.getY();
         
        int rad = getRadius();
        
        //원본 맵을 그리고
        map.draw(c, g2d);
            
        float dark = 0.90f;
        
        // 검은 마스크를 씌움...
        g2d.setColor(new Color(0, 0, 0, (int)(255 * dark)));
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        
        if (isTurnOnFlash) {
            

            java.awt.Polygon arc = pr(getPosition(), getAngle()); 

            g2d.setClip(arc); // 밝은 영역
            map.draw(c, g2d); // 그 부분만 그려짐
            g2d.setPaint(new RadialGradientPaint(x, y, 400f,
                                                                new float[] { 0.7f, 1f },
                                                                new Color[] {
                                                                    new Color(0, 0, 0, (int) (255 * 0)),
                                                                    new Color(0, 0, 0, (int) (255 * dark))
                                                                })); // 그라데이션 삽입
            g2d.fill(arc); // 채움 
            
            
            g2d.setClip(null); // 초기화
            
            //g2d.drawImage(Main.draw(g2d, x, y, dx, dy, 0.3, Color.black, Color.black), 0, 0, null);
        } 
        
        
        g2d.drawImage(sp.getSprite("player.png", (int) (i % 3), getGridIndex(), 36, 82), (int) x - rad / 2, (int) y - rad, null);
        
        //g2d.setColor(Color.CYAN);
        //g2d.drawLine((int) x , (int) y, dx, dy);
        //g2d.drawOval((int) x - rad, (int) y - rad, rad * 2, rad * 2);
    }
    
    private double i = 0;
 
    public static double normalAbsoluteAngleDegrees(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }
    
    private int getGridIndex() {
        double ang = normalAbsoluteAngleDegrees(Math.toDegrees(getAngle()));
        
        if (45 <= ang && ang < 135) {
            return 0;
        }
        
        if (135 <= ang && ang < 225) {
            return 1;
        }
        
        if (225 <= ang && ang < 315) {
            return 3;
        }
        
        return 2;
    }

    @Override
    public void update(GameLoop c) {
        Game g= (Game) c;
        if (g.w) vel.setY(-4);
        if (g.s) vel.setY(4);
        if (g.a) vel.setX(-4);
        if (g.d) vel.setX(4);
        
        if ( ! (g.w || g.s)) vel.setY(0);
        if ( ! (g.a || g.d)) vel.setX(0);
        
        Point2D p = getPosition();
        
        if (vel.getX() != 0 || vel.getY() != 0) {
            i += 0.333;
        }
        
        Vector2D v = new Vector2D(vel);
        int x = (int) (p.getX() + vel.getX());
        int y = (int) (p.getY() + vel.getY());

        p.set(x, y);
        
        for(Wall w : map.getWall()) {

            SAT.Response r = new SAT.Response();
            if (SAT.testPolygonCircle(w, this, r)) {

                v = r.overlapV.add(p);

                p.set((int) (v.getX()), (int) (v.getY()));
            }
        }
        
    }
}