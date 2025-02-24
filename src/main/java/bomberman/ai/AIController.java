package bomberman.ai;

import bomberman.entities.Enemy;
import bomberman.entities.Player;
import java.util.Random;

public class AIController {
    private static final int DETECTION_RANGE = 3;
    private static final double CHASE_PROBABILITY = 0.5;
    private final Enemy enemy;
    private final Random random;
    private int targetX;
    private int targetY;

    public AIController(Enemy enemy) {
        this.enemy = enemy;
        this.random = new Random();
        this.targetX = enemy.getXTile();
        this.targetY = enemy.getYTile();
    }

    public void update(Player player) {
        if (isPlayerInRange(player)) {
            if (shouldChase()) {
                chasePlayer(player);
            } else {
                moveRandomly();
            }
        } else {
            moveRandomly();
        }
    }

    private boolean isPlayerInRange(Player player) {
        int dx = Math.abs(enemy.getXTile() - player.getXTile());
        int dy = Math.abs(enemy.getYTile() - player.getYTile());
        return (dx + dy) <= DETECTION_RANGE;
    }

    private boolean shouldChase() {
        return random.nextDouble() < CHASE_PROBABILITY;
    }

    private void chasePlayer(Player player) {
        if (random.nextInt(100) < 30) {
            targetX = player.getXTile();
            targetY = player.getYTile();
        }
        calculateMovement();
    }

    private void moveRandomly() {
        if (enemy.getXTile() == targetX && enemy.getYTile() == targetY) {
            targetX = enemy.getXTile() + random.nextInt(3) - 1;
            targetY = enemy.getYTile() + random.nextInt(3) - 1;
        }
        calculateMovement();
    }

    private void calculateMovement() {
        int dx = Integer.compare(targetX, enemy.getXTile());
        int dy = Integer.compare(targetY, enemy.getYTile());

        if (dx != 0) {
            enemy.move(dx, 0);
        } else if (dy != 0) {
            enemy.move(0, dy);
        }
    }
}