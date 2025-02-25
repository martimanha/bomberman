package bomberman.entities;

import bomberman.ai.AIController;
import bomberman.managers.CollisionManager;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static bomberman.GameConstants.*;

public class Enemy {
    private double pixelX, pixelY;
    private int targetXTile, targetYTile;
    private boolean isMoving = false;
    private final Map<Player.Direction, BufferedImage> sprites = new EnumMap<>(Player.Direction.class);
    private final Random random = new Random();
    private long lastMoveTime;
    private boolean isAlive = true;
    private Player.Direction currentDirection;
    private final List<Enemy> allEnemies;
    private final AIController aiController;

    public Enemy(int startXTile, int startYTile, List<Enemy> allEnemies) {
        this.targetXTile = startXTile;
        this.targetYTile = startYTile;
        this.pixelX = startXTile * TILE_SIZE;
        this.pixelY = startYTile * TILE_SIZE;
        this.currentDirection = Player.Direction.DOWN;
        this.allEnemies = allEnemies;
        this.aiController = new AIController(this, allEnemies);
        this.lastMoveTime = System.currentTimeMillis();
        loadSprites();
    }

    private void loadSprites() {
        sprites.put(Player.Direction.UP, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_up.png"));
        sprites.put(Player.Direction.DOWN, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_down.png"));
        sprites.put(Player.Direction.LEFT, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_left.png"));
        sprites.put(Player.Direction.RIGHT, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_right.png"));
    }

    public void update(Player player) {
        if (!isAlive) return;

        aiController.update(player);

        if (System.currentTimeMillis() - lastMoveTime > ENEMY_MOVE_INTERVAL) {
            attemptMove();
            lastMoveTime = System.currentTimeMillis();
        }

        updatePosition();
        checkExplosionCollision();
    }

    private void checkExplosionCollision() {
        if (CollisionManager.checkExplosionCollision(targetXTile, targetYTile)) {
            isAlive = false;
        }
    }

    private void updatePosition() {
        if (isMoving) {
            double speed = 0.1;
            double targetX = targetXTile * TILE_SIZE;
            double targetY = targetYTile * TILE_SIZE;

            pixelX += (targetX - pixelX) * speed;
            pixelY += (targetY - pixelY) * speed;

            if (Math.abs(pixelX - targetX) < 0.5 && Math.abs(pixelY - targetY) < 0.5) {
                pixelX = targetX;
                pixelY = targetY;
                isMoving = false;
            }
        }
    }

    private void attemptMove() {
        int direction = random.nextInt(4);
        int dx = 0, dy = 0;

        switch (direction) {
            case 0: dy = -1; break;
            case 1: dy = 1; break;
            case 2: dx = -1; break;
            case 3: dx = 1; break;
        }

        move(dx, dy);
    }

    public void move(int dx, int dy) {
        int newX = targetXTile + dx;
        int newY = targetYTile + dy;

        if (CollisionManager.canEnemyMoveTo(newX, newY, allEnemies)) {
            targetXTile = newX;
            targetYTile = newY;

            if (dx > 0) currentDirection = Player.Direction.RIGHT;
            else if (dx < 0) currentDirection = Player.Direction.LEFT;
            else if (dy > 0) currentDirection = Player.Direction.DOWN;
            else if (dy < 0) currentDirection = Player.Direction.UP;

            isMoving = true;
        }
    }

    public void draw(Graphics2D g2) {
        if (isAlive) {
            BufferedImage currentSprite = sprites.get(currentDirection);
            g2.drawImage(currentSprite, (int) pixelX, (int) pixelY, TILE_SIZE, TILE_SIZE, null);
        }
    }

    // Getters
    public int getXTile() { return targetXTile; }
    public int getYTile() { return targetYTile; }
    public boolean isAlive() { return isAlive; }
    public double getPixelX() { return pixelX; }
    public double getPixelY() { return pixelY; }
}