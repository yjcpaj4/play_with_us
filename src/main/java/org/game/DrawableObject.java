package org.game;

import org.game.GameLoop;
import java.awt.Graphics2D;

public interface DrawableObject {
    
    void update(GameLoop g);

    void draw(GameLoop g, Graphics2D g2d);
}