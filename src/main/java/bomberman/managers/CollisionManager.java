package bomberman.managers;

import bomberman.entities.Bomb;
import bomberman.entities.Enemy;
import bomberman.entities.Explosion;
import bomberman.powerups.PowerUp;
import bomberman.powerups.PowerUpType;
import java.util.List;
import static bomberman.GameConstants.*;

public class CollisionManager {
    private static char[][] map;
    private static List<Bomb> bombs;
    private static List<PowerUp> powerUps;
    private static List<Explosion> explosions;

    public static void initialize(char[][] gameMap, List<Bomb> activeBombs,
                                  List<PowerUp> activePowerUps, List<Explosion> activeExplosions) {
        map = gameMap;
        bombs = activeBombs;
        powerUps = activePowerUps;
        explosions = activeExplosions;
    }

    public static boolean canMoveTo(int xTile, int yTile, boolean isInvulnerable) {
        if (isOutOfBounds(xTile, yTile)) return false;
        if (isSolidBlock(xTile, yTile)) return false;
        if (isBombAtPosition(xTile, yTile)) return false;
        return isInvulnerable || !checkExplosionCollision(xTile, yTile);
    }

    public static boolean canEnemyMoveTo(int xTile, int yTile, List<Enemy> enemies) {
        if (isOutOfBounds(xTile, yTile)) return false;
        if (isSolidBlock(xTile, yTile)) return false;
        return !isEnemyAtPosition(xTile, yTile, enemies) && !isBombAtPosition(xTile, yTile);
    }

    public static void destroyBlock(int x, int y) {
        if (map[y][x] == 'B') {
            map[y][x] = 'V';
            tryGeneratePowerUp(x, y);
        }
    }

    public static boolean checkExplosionCollision(int xTile, int yTile) {
        if (isOutOfBounds(xTile, yTile) || map[yTile][xTile] == 'E') {
            return false;
        }

        return explosions.stream()
                .flatMap(explosion -> explosion.getSegments().stream())
                .anyMatch(seg -> seg[0] == xTile && seg[1] == yTile);
    }

    // Novo método para acesso às explosões
    public static List<Explosion> getExplosions() {
        return explosions;
    }

    public static char[][] getMap() {
        return map;
    }

    private static boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= MAP_COLS || y < 0 || y >= MAP_ROWS;
    }

    public static boolean isSolidBlock(int x, int y) {
        if (isOutOfBounds(x, y)) return true;
        char tile = map[y][x];
        return tile == 'H' || tile == 'B';
    }

    private static boolean isBombAtPosition(int x, int y) {
        return bombs.stream().anyMatch(b -> b.getXTile() == x && b.getYTile() == y);
    }

    private static boolean isEnemyAtPosition(int x, int y, List<Enemy> enemies) {
        return enemies.stream().anyMatch(e -> e.getXTile() == x && e.getYTile() == y);
    }

    private static void tryGeneratePowerUp(int x, int y) {
        if (Math.random() < BASE_POWERUP_CHANCE) {
            PowerUpType type = (Math.random() < POSITIVE_POWERUP_CHANCE) ?
                    PowerUpType.getRandomPositive() : PowerUpType.getRandomNegative();
            powerUps.add(new PowerUp(type, x, y));
        }
    }
}