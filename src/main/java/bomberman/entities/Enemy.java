package bomberman.entities;

import bomberman.ai.AIController;
import bomberman.managers.CollisionManager;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static bomberman.GameConstants.*;

public class Enemy {
    private double pixelX, pixelY;
    private int targetXTile, targetYTile;
    private boolean isMoving = false;
    private final Map<Player.Direction, BufferedImage> sprites = new EnumMap<>(Player.Direction.class);
    private boolean isAlive = true;
    private Player.Direction currentDirection;
    private final List<Enemy> allEnemies;
    private final AIController aiController;
    private long lastMoveTime; // Novo campo para controle de tempo

    public Enemy(int startXTile, int startYTile, List<Enemy> allEnemies) {
        this.targetXTile = startXTile;
        this.targetYTile = startYTile;
        this.pixelX = startXTile * TILE_SIZE;
        this.pixelY = startYTile * TILE_SIZE;
        this.currentDirection = Player.Direction.DOWN;
        this.allEnemies = allEnemies;
        this.aiController = new AIController(this, allEnemies);
        this.lastMoveTime = System.currentTimeMillis(); // Inicializa o tempo
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
        updatePosition();
        checkExplosionCollision();
    }

    private void checkExplosionCollision() {
        int currentXTile = (int)(pixelX / TILE_SIZE);
        int currentYTile = (int)(pixelY / TILE_SIZE);

        if (CollisionManager.checkExplosionCollision(currentXTile, currentYTile)) {
            isAlive = false;
        }
    }

    private void updatePosition() {
        if (isMoving) {
            double speed = ENEMY_BASE_SPEED; // Usando constante nova
            double targetX = targetXTile * TILE_SIZE;
            double targetY = targetYTile * TILE_SIZE;

            pixelX += (targetX - pixelX) * speed;
            pixelY += (targetY - pixelY) * speed;

            if (Math.abs(pixelX - targetX) < 0.5 && Math.abs(pixelY - targetY) < 0.5) {
                pixelX = targetX;
                pixelY = targetY;
                isMoving = false;
                lastMoveTime = System.currentTimeMillis(); // Atualiza ao terminar movimento
            }
        }
    }

    public void move(int dx, int dy) {
        int newX = targetXTile + dx;
        int newY = targetYTile + dy;

        if (CollisionManager.canEnemyMoveTo(newX, newY, allEnemies)) {
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
            g2.drawImage(currentSprite, (int) pixelX, (int) pixelY, TILE_SIZE, TILE_SIZE, null);
        }
    }

    // Novo getter para o tempo do último movimento
    public long getLastMoveTime() {
        return lastMoveTime;
    }

    // Getters
    public int getXTile() { return targetXTile; }
    public int getYTile() { return targetYTile; }
    public boolean isAlive() { return isAlive; }
    public boolean isMoving() { return isMoving; }
    public double getPixelX() { return pixelX; }
    public double getPixelY() { return pixelY; }
}