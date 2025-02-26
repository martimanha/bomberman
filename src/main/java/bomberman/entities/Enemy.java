package bomberman.entities;

import bomberman.ai.AIController;
import bomberman.managers.CollisionManager;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import static bomberman.GameConstants.*;

public class Enemy {
    private double pixelX, pixelY;
    private int targetXTile, targetYTile;
    private boolean isMoving = false;
    private boolean isAlive = true;
    private final Point originalSpawn;
    private long deathTime;
    private final AIController aiController;
    private long lastMoveTime;
    private Player.Direction currentDirection;
    private final Map<Player.Direction, BufferedImage> sprites = new EnumMap<>(Player.Direction.class);

    public Enemy(int startXTile, int startYTile, List<Enemy> allEnemies) {
        this.originalSpawn = new Point(startXTile, startYTile);
        this.targetXTile = startXTile;
        this.targetYTile = startYTile;
        this.pixelX = startXTile * TILE_SIZE;
        this.pixelY = startYTile * TILE_SIZE;
        this.aiController = new AIController(this, allEnemies);
        this.lastMoveTime = System.currentTimeMillis();
        this.currentDirection = Player.Direction.DOWN;
        loadSprites();
    }

    private void loadSprites() {
        sprites.put(Player.Direction.UP, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_up.png"));
        sprites.put(Player.Direction.DOWN, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_down.png"));
        sprites.put(Player.Direction.LEFT, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_left.png"));
        sprites.put(Player.Direction.RIGHT, SpriteLoader.loadSprite("/sprites/entities/enemy/enemy_right.png"));
    }

    public void update(Player player) {
        if (!isAlive) {
            checkRespawn();
            return;
        }

        aiController.update(player);
        updatePosition();
        checkExplosionCollision();
    }

    private void checkRespawn() {
        if (System.currentTimeMillis() - deathTime >= 15000) {
            respawn();
        }
    }

    private void respawn() {
        isAlive = true;
        targetXTile = originalSpawn.x;
        targetYTile = originalSpawn.y;
        pixelX = targetXTile * TILE_SIZE;
        pixelY = targetYTile * TILE_SIZE;
        isMoving = false;
    }

    private void updatePosition() {
        if (isMoving) {
            double speed = ENEMY_BASE_SPEED;
            double targetX = targetXTile * TILE_SIZE;
            double targetY = targetYTile * TILE_SIZE;

            pixelX += (targetX - pixelX) * speed;
            pixelY += (targetY - pixelY) * speed;

            if (Math.abs(pixelX - targetX) < 0.5 && Math.abs(pixelY - targetY) < 0.5) {
                pixelX = targetX;
                pixelY = targetY;
                isMoving = false;
                lastMoveTime = System.currentTimeMillis();
            }
        }
    }

    private void checkExplosionCollision() {
        if (CollisionManager.checkExplosionCollision(targetXTile, targetYTile)) {
            die();
        }
    }

    private void die() {
        isAlive = false;
        deathTime = System.currentTimeMillis();
    }

    public void move(int dx, int dy) {
        int newX = targetXTile + dx;
        int newY = targetYTile + dy;

        if (CollisionManager.canEnemyMoveTo(newX, newY, aiController.getAllEnemies())) {
            targetXTile = newX;
            targetYTile = newY;
            isMoving = true;

            if (dx > 0) currentDirection = Player.Direction.RIGHT;
            else if (dx < 0) currentDirection = Player.Direction.LEFT;
            else if (dy > 0) currentDirection = Player.Direction.DOWN;
            else if (dy < 0) currentDirection = Player.Direction.UP;
        }
    }

    public void draw(Graphics2D g2) {
        if (isAlive) {
            BufferedImage currentSprite = sprites.get(currentDirection);
            g2.drawImage(currentSprite, (int)pixelX, (int)pixelY, TILE_SIZE, TILE_SIZE, null);
        }
    }

    public int getXTile() { return targetXTile; }
    public int getYTile() { return targetYTile; }
    public boolean isAlive() { return isAlive; }
    public boolean isMoving() { return isMoving; }
    public long getLastMoveTime() { return lastMoveTime; }
    public double getPixelX() { return pixelX; }
    public double getPixelY() { return pixelY; }
}