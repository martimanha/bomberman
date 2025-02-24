package bomberman.entities;

import bomberman.managers.CollisionManager;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static bomberman.GameConstants.*;

public class Bomb {
    private final int xTile;
    private final int yTile;
    private final int power;
    private long placedTime;
    private boolean exploded = false;

    public Bomb(int xTile, int yTile, int power) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.power = power;
        this.placedTime = System.currentTimeMillis();
    }

    public void update() {
        if (!exploded && System.currentTimeMillis() - placedTime > BOMB_FUSE_TIME) {
            explode();
        }
    }

    private void explode() {
        Explosion tempExplosion = new Explosion(xTile, yTile, power);
        tempExplosion.getSegments().forEach(segment -> {
            int x = segment[0];
            int y = segment[1];
            CollisionManager.destroyBlock(x, y);
        });
        exploded = true;
    }

    public void draw(Graphics2D g2) {
        BufferedImage sprite = SpriteLoader.loadSprite("/sprites/entities/bomb.png");
        int x = xTile * TILE_SIZE;
        int y = yTile * TILE_SIZE;
        g2.drawImage(sprite, x, y, TILE_SIZE, TILE_SIZE, null);
    }

    public boolean hasExploded() {
        return exploded;
    }

    public int getXTile() { return xTile; }
    public int getYTile() { return yTile; }
    public int getPower() { return power; }
}