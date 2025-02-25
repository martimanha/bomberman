package bomberman.ai;

import bomberman.entities.Enemy;
import bomberman.entities.Player;
import bomberman.managers.CollisionManager;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static bomberman.GameConstants.*;

public class AIController {
    private static final int PLAYER_DETECTION_RADIUS = 5;
    private static final int EXPLOSION_ESCAPE_RADIUS = 3;
    private static final double CHASE_PLAYER_CHANCE = 0.6;
    private static final double ESCAPE_EXPLOSION_CHANCE = 0.4;
    private static final int[][] DIRECTIONS = {{1,0}, {-1,0}, {0,1}, {0,-1}};
    private static final int ENEMY_MOVE_DELAY = 1000;

    private final Enemy enemy;
    private final List<Enemy> allEnemies;
    private final Pathfinder pathfinder;
    private final Random random = new Random();

    public AIController(Enemy enemy, List<Enemy> allEnemies) {
        this.enemy = enemy;
        this.allEnemies = allEnemies;
        this.pathfinder = new Pathfinder(allEnemies);
    }

    public void update(Player player) {
        if (player == null ||
                enemy.isMoving() ||
                (System.currentTimeMillis() - enemy.getLastMoveTime()) < ENEMY_MOVE_DELAY) {
            return;
        }

        Point currentPos = new Point(enemy.getXTile(), enemy.getYTile());

        // Prioridade 1: Fugir de explosões
        if (shouldEscapeExplosions(currentPos)) {
            moveAwayFromExplosions(currentPos);
            return;
        }

        // Prioridade 2: Perseguir jogador
        if (shouldChasePlayer(player, currentPos)) {
            chasePlayer(player, currentPos);
            return;
        }

        // Movimento aleatório padrão
        moveRandomly();
    }

    private boolean shouldEscapeExplosions(Point currentPos) {
        return isNearExplosion(currentPos) && random.nextDouble() < ESCAPE_EXPLOSION_CHANCE;
    }

    private boolean isNearExplosion(Point currentPos) {
        return CollisionManager.getExplosions().stream()
                .flatMap(explosion -> explosion.getSegments().stream())
                .anyMatch(seg -> distance(currentPos, new Point(seg[0], seg[1])) <= EXPLOSION_ESCAPE_RADIUS);
    }

    private boolean shouldChasePlayer(Player player, Point currentPos) {
        return isPlayerInRange(player, currentPos) && random.nextDouble() < CHASE_PLAYER_CHANCE;
    }

    private boolean isPlayerInRange(Player player, Point currentPos) {
        Point playerPos = new Point(player.getXTile(), player.getYTile());
        return distance(currentPos, playerPos) <= PLAYER_DETECTION_RADIUS;
    }

    private void moveAwayFromExplosions(Point currentPos) {
        List<Point> safeDirections = new ArrayList<>();

        for (int[] dir : DIRECTIONS) {
            int newX = currentPos.x + dir[0];
            int newY = currentPos.y + dir[1];

            if (isPositionSafe(newX, newY)) {
                safeDirections.add(new Point(dir[0], dir[1]));
            }
        }

        if (!safeDirections.isEmpty()) {
            Point direction = safeDirections.get(random.nextInt(safeDirections.size()));
            enemy.move(direction.x, direction.y);
        } else {
            moveRandomly();
        }
    }

    private boolean isPositionSafe(int x, int y) {
        return CollisionManager.canEnemyMoveTo(x, y, allEnemies) &&
                CollisionManager.getExplosions().stream()
                        .noneMatch(explosion -> explosion.getSegments().stream()
                                .anyMatch(seg -> distance(new Point(x, y), new Point(seg[0], seg[1])) <= EXPLOSION_ESCAPE_RADIUS));
    }

    private void chasePlayer(Player player, Point currentPos) {
        Point target = new Point(player.getXTile(), player.getYTile());
        List<Point> path = pathfinder.findPath(currentPos, target);

        if (!path.isEmpty()) {
            Point nextStep = path.get(0);
            int dx = nextStep.x - currentPos.x;
            int dy = nextStep.y - currentPos.y;
            enemy.move(dx, dy);
        }
    }

    private void moveRandomly() {
        int[] dir = DIRECTIONS[random.nextInt(4)];
        enemy.move(dir[0], dir[1]);
    }

    private double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}