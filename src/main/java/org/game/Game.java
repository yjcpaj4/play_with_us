package org.game;

import org.game.resource.ResourceManager;
import org.game.resource.ImageResource;
import org.game.resource.SpriteImageResource;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import org.game.map.Map;

public class Game extends GraphicLooper implements MouseMotionListener, MouseListener, KeyListener {

    public static final boolean DEBUG = true;
               
    private Map mMap;
    private InputManager mInput = InputManager.getInstance();
    private ResourceManager mRes = ResourceManager.getInstance();

    public Game() { 
        mCanvas.addMouseListener(this);
        mCanvas.addMouseMotionListener(this);
        mCanvas.addKeyListener(this);
        
        mMap = new Map();
        
        try {
            loadResources();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void loadResources() throws Exception {
        mRes.load("./res/img_map.png",    "map");
        mRes.load("./res/img_player.png", "player.walk.n");
        mRes.load("./res/img_player.png", "player.walk.ne");
        mRes.load("./res/img_player.png", "player.walk.e");
        mRes.load("./res/img_player.png", "player.walk.se");
        mRes.load("./res/img_player.png", "player.walk.s");
        mRes.load("./res/img_player.png", "player.walk.sw");
        mRes.load("./res/img_player.png", "player.walk.w");
        mRes.load("./res/img_player.png", "player.walk.nw");
    }
    
    @Override
    protected void draw(Graphics2D g2d) {  
        super.draw(g2d);
        
        mMap.update(this);
        mMap.draw(this, g2d);
    }
    
    public InputManager getInput() {
        return mInput;
    }
    
    public BufferedImage getImage(String k) {
        return ((ImageResource) mRes.getImage(k)).getImageData();
    }
    
    public SpriteImageResource getSprite(String s) {
        return ((SpriteImageResource) mRes.getSprite(s));
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mInput.setKeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mInput.setKeyRelease(e.getKeyCode());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mInput.setMousePosition(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mInput.setMousePress(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mInput.setMouseRelease(e.getButton());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}  