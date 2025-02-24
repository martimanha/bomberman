package bomberman.rendering;

import bomberman.powerups.PowerUp;
import bomberman.entities.*;
import java.awt.Graphics2D;
import java.util.List;
import static bomberman.GameConstants.TILE_SIZE;

public class EntityRenderer {
    public static void drawPlayer(Graphics2D g2, Player player) {
        if (player.isAlive()) {
            player.draw(g2);
        }
    }

    public static void drawEnemies(Graphics2D g2, List<Enemy> enemies) {
        enemies.stream()
                .filter(Enemy::isAlive)
                .forEach(enemy -> enemy.draw(g2));
    }

    public static void drawBombs(Graphics2D g2, List<Bomb> bombs) {
        bombs.forEach(bomb -> {
            if (!bomb.hasExploded()) {
                bomb.draw(g2);
            }
        });
    }

    public static void drawExplosions(Graphics2D g2, List<Explosion> explosions) {
        explosions.stream()
                .filter(explosion -> !explosion.isFinished())
                .forEach(explosion -> explosion.draw(g2));
    }

    public static void drawPowerUps(Graphics2D g2, List<PowerUp> powerUps) {
        powerUps.stream()
                .filter(PowerUp::isActive)
                .forEach(powerUp -> {
                    int x = powerUp.getXTile() * TILE_SIZE;
                    int y = powerUp.getYTile() * TILE_SIZE;
                    g2.drawImage(powerUp.getSprite(), x, y, TILE_SIZE, TILE_SIZE, null);
                });
    }
}