package bomberman.powerups;

import bomberman.utils.PowerUpLoader;
import static bomberman.GameConstants.POWERUP_DURATION;
import java.awt.image.BufferedImage;

public class PowerUp {
    private final PowerUpType type;
    private final int xTile;
    private final int yTile;
    private final long spawnTime;
    private boolean active = true;

    public PowerUp(PowerUpType type, int xTile, int yTile) {
        this.type = type;
        this.xTile = xTile;
        this.yTile = yTile;
        this.spawnTime = System.currentTimeMillis();
    }

    public void update() {
        if (System.currentTimeMillis() - spawnTime > POWERUP_DURATION) {
            active = false;
        }
    }

    public void applyEffect(bomberman.managers.StatusManager statusManager) {
        statusManager.applyPowerUpEffect(type);
        active = false;
    }

    public BufferedImage getSprite() {
        return PowerUpLoader.getSprite(type); // Correção aqui
    }

    // Getters
    public boolean isActive() { return active; }
    public boolean isPositive() { return type.isPositive(); }
    public int getXTile() { return xTile; }
    public int getYTile() { return yTile; }
    public PowerUpType getType() { return type; }
}