package bomberman.ui;

import bomberman.utils.UILoader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button {
    protected BufferedImage normalImage;
    protected BufferedImage hoverImage;
    protected Rectangle bounds;
    protected boolean isHovered;

    public Button(String normalPath, String hoverPath, int x, int y) {
        this.normalImage = UILoader.loadUISprite(normalPath);
        this.hoverImage = UILoader.loadUISprite(hoverPath);
        this.bounds = new Rectangle(x, y, normalImage.getWidth(), normalImage.getHeight());
        this.isHovered = false;
    }

    public void update(Point mousePosition) {
        isHovered = bounds.contains(mousePosition);
    }

    public void render(Graphics2D g2) {
        BufferedImage currentImage = isHovered ? hoverImage : normalImage;
        g2.drawImage(currentImage, getX(), getY(), null);
    }

    public boolean isClicked(MouseEvent e) {
        return bounds.contains(e.getPoint());
    }

    public void reset() {
        isHovered = false;
    }

    // Getters
    public int getX() { return bounds.x; }
    public int getY() { return bounds.y; }
    public int getWidth() { return bounds.width; }
    public int getHeight() { return bounds.height; }
    public boolean isHovered() { return isHovered; }
}