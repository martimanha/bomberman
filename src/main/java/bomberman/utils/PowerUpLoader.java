package bomberman.utils;

import bomberman.powerups.PowerUpType;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

public class PowerUpLoader {
    private static final Map<PowerUpType, BufferedImage> sprites = new EnumMap<>(PowerUpType.class);

    static {
        // Powerups Positivos
        sprites.put(PowerUpType.BOMB_POWER_UP, loadSprite("bomb_powerup"));
        sprites.put(PowerUpType.SPEED_UP, loadSprite("speed_powerup"));
        sprites.put(PowerUpType.HEALTH_UP, loadSprite("health_powerup"));
        sprites.put(PowerUpType.LUCK_UP, loadSprite("luck_powerup"));

        // Powerups Negativos
        sprites.put(PowerUpType.BOMB_POWER_DOWN, loadSprite("bomb_powerup"));
        sprites.put(PowerUpType.SPEED_DOWN, loadSprite("speed_powerup"));
        sprites.put(PowerUpType.HEALTH_DOWN, loadSprite("health_powerup"));
        sprites.put(PowerUpType.LUCK_DOWN, loadSprite("luck_powerup"));
        sprites.put(PowerUpType.ENEMY_DAMAGE_UP, loadSprite("enemy_powerup"));
    }

    private static BufferedImage loadSprite(String path) {
        return SpriteLoader.loadSprite("/sprites/powerups/" + path + ".png");
    }

    public static BufferedImage getSprite(PowerUpType type) {
        BufferedImage sprite = sprites.get(type);
        if (sprite == null) {
            System.err.println("Sprite de powerup não encontrado: " + type);
            return SpriteLoader.loadSprite("/sprites/powerups/powerup.png");
        }
        return sprite;
    }
}