package bomberman.entities;

import bomberman.managers.CollisionManager;
import bomberman.managers.StatusManager;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import static bomberman.GameConstants.*;

public class Player {
    private double pixelX, pixelY;
    private int targetXTile, targetYTile;
    private boolean isMoving = false;
    private final Map<Direction, BufferedImage> sprites = new EnumMap<>(Direction.class);
    private boolean isInvulnerable = false;
    private long damageTime;
    private long lastBombTime;
    private Direction currentDirection;
    private final StatusManager statusManager;
    private boolean isAlive = true;

    public enum Direction {
        UP(0, -1),
        DOWN(0, 1),
        LEFT(-1, 0),
        RIGHT(1, 0);

        public final int xOffset;
        public final int yOffset;

        Direction(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
        }
    }

    public Player(int startXTile, int startYTile, StatusManager statusManager) {
        this.targetXTile = startXTile;
        this.targetYTile = startYTile;
        this.pixelX = startXTile * TILE_SIZE;
        this.pixelY = startYTile * TILE_SIZE;
        this.currentDirection = Direction.RIGHT;
        this.statusManager = statusManager;
        this.lastBombTime = 0;
        loadSprites();
    }

    private void loadSprites() {
        sprites.put(Direction.UP, SpriteLoader.loadSprite("/sprites/entities/player/player_up.png"));
        sprites.put(Direction.DOWN, SpriteLoader.loadSprite("/sprites/entities/player/player_down.png"));
        sprites.put(Direction.LEFT, SpriteLoader.loadSprite("/sprites/entities/player/player_left.png"));
        sprites.put(Direction.RIGHT, SpriteLoader.loadSprite("/sprites/entities/player/player_right.png"));
    }

    public void move(Direction dir) {
        if (isMoving || isInvulnerable || !isAlive) return;

        int newX = targetXTile + dir.xOffset;
        int newY = targetYTile + dir.yOffset;

        if (CollisionManager.canMoveTo(newX, newY) && !isInExplosion(newX, newY)) {
            targetXTile = newX;
            targetYTile = newY;
            currentDirection = dir;
            isMoving = true;
        }
    }

    private boolean isInExplosion(int x, int y) {
        return CollisionManager.checkExplosionCollision(x, y);
    }

    public void update() {
        if (!isAlive) return;

        updatePosition();
        checkExplosionCollision();
        updateInvulnerability();
    }

    private void updatePosition() {
        if (isMoving) {
            double speed = PLAYER_BASE_SPEED * statusManager.getSpeedMultiplier();
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

    private void checkExplosionCollision() {
        if (!isInvulnerable && CollisionManager.checkExplosionCollision(targetXTile, targetYTile)) {
            takeDamage();
        }
    }

    private void updateInvulnerability() {
        if (isInvulnerable && System.currentTimeMillis() - damageTime > PLAYER_INVULNERABILITY_DURATION) {
            isInvulnerable = false;
        }
    }

    public void takeDamage() {
        if (!isInvulnerable && isAlive) {
            statusManager.loseLife();
            activateInvulnerability();

            if (statusManager.getLives() <= 0) {
                isAlive = false;
            }
        }
    }

    private void activateInvulnerability() {
        isInvulnerable = true;
        damageTime = System.currentTimeMillis();
    }

    public void resetPosition(int xTile, int yTile) {
        this.targetXTile = xTile;
        this.targetYTile = yTile;
        this.pixelX = xTile * TILE_SIZE;
        this.pixelY = yTile * TILE_SIZE;
        this.isMoving = false;
        this.isInvulnerable = false;
        this.currentDirection = Direction.RIGHT;
        this.isAlive = true;
        this.statusManager.reset();
    }

    public void draw(Graphics2D g2) {
        if (isAlive) {
            if (isInvulnerable) {
                if ((System.currentTimeMillis() / 200) % 2 == 0) {
                    drawSprite(g2);
                }
            } else {
                drawSprite(g2);
            }
        }
    }

    private void drawSprite(Graphics2D g2) {
        BufferedImage currentSprite = sprites.get(currentDirection);
        g2.drawImage(currentSprite, (int) pixelX, (int) pixelY, TILE_SIZE, TILE_SIZE, null);
    }

    // Getters
    public int getXTile() { return targetXTile; }
    public int getYTile() { return targetYTile; }
    public long getLastBombTime() { return lastBombTime; }
    public void setLastBombTime(long time) { this.lastBombTime = time; }
    public boolean isAlive() { return isAlive; }
    public Direction getCurrentDirection() { return currentDirection; }
    public StatusManager getStatusManager() { return statusManager; }
}