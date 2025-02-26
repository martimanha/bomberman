package bomberman.managers;

import bomberman.entities.Bomb;
import bomberman.entities.Enemy;
import bomberman.entities.Explosion;
import bomberman.entities.Player;
import java.util.List;

public class GameStateManager {
    private final List<Enemy> enemies;
    private final List<Bomb> bombs;
    private final List<Explosion> explosions;
    private final Player player;
    private char[][] gameMap;
    private final StatusManager statusManager;

    public GameStateManager(List<Enemy> enemies, List<Bomb> bombs,
                            List<Explosion> explosions, Player player) {
        this.enemies = enemies;
        this.bombs = bombs;
        this.explosions = explosions;
        this.player = player;
        this.statusManager = player.getStatusManager();
    }

    public void updateGameState() {
        updateEnemies();
        updateBombs();
        updateExplosions();
        checkPlayerStatus();
    }

    private void updateEnemies() {
        enemies.forEach(enemy -> enemy.update(player));
    }

    private void updateBombs() {
        bombs.removeIf(Bomb::hasExploded);
    }

    private void updateExplosions() {
        explosions.removeIf(explosion -> {
            if (explosion.isFinished()) {
                explosion.getSegments().forEach(seg -> {
                    int x = seg[0];
                    int y = seg[1];
                    if (gameMap[y][x] == 'B') gameMap[y][x] = 'V';
                });
                return true;
            }
            return false;
        });
    }

    private void checkPlayerStatus() {
        if (!statusManager.isAlive()) {
        }
    }

    public void resetGameState() {
        enemies.clear();
        bombs.clear();
        explosions.clear();
        MapLoader.LoadResult result = MapLoader.loadMap("/maps/level1.csv");
        this.gameMap = result.map;
        this.enemies.addAll(result.enemies);
        player.resetPosition(result.playerX, result.playerY);
        statusManager.reset();
    }

    public char[][] getGameMap() {
        return gameMap;
    }
}