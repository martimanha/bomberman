package bomberman.entities;

import bomberman.managers.CollisionManager;
import bomberman.utils.SpriteLoader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static bomberman.GameConstants.*;

public class Explosion {
    private final List<int[]> segments = new ArrayList<>();
    private final BufferedImage sprite;
    private final long startTime;
    private final int power;

    public Explosion(int centerX, int centerY, int power) {
        this.sprite = SpriteLoader.loadSprite("/sprites/effects/explosion.png");
        this.startTime = System.currentTimeMillis();
        this.power = power;
        createExplosionPattern(centerX, centerY);
    }

    private void createExplosionPattern(int centerX, int centerY) {
        addValidSegment(centerX, centerY);
        expandDirection(centerX, centerY, 1, 0);  // Direita
        expandDirection(centerX, centerY, -1, 0); // Esquerda
        expandDirection(centerX, centerY, 0, 1);  // Baixo
        expandDirection(centerX, centerY, 0, -1); // Cima
    }

    private void expandDirection(int startX, int startY, int dx, int dy) {
        for (int i = 1; i <= power; i++) {
            int x = startX + (dx * i);
            int y = startY + (dy * i);

            if (!isValidPosition(x, y)) break;

            char tile = CollisionManager.getMap()[y][x];
            if (tile == 'H') break; // Parede indestrutível

            addValidSegment(x, y);

            if (tile == 'B' || tile == 'S') { // Destruir 'S' também
                CollisionManager.destroyBlock(x, y);
            }
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < MAP_COLS && y >= 0 && y < MAP_ROWS;
    }

    private void addValidSegment(int x, int y) {
        if (CollisionManager.getMap()[y][x] != 'E') { // Ignora tiles de spawn de inimigos
            segments.add(new int[]{x, y});
        }
    }

    public void draw(Graphics2D g2) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < EXPLOSION_DURATION) {
            segments.forEach(segment -> {
                int x = segment[0] * TILE_SIZE;
                int y = segment[1] * TILE_SIZE;
                g2.drawImage(sprite, x, y, TILE_SIZE, TILE_SIZE, null);
            });
        }
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - startTime > EXPLOSION_DURATION;
    }

    public List<int[]> getSegments() {
        return new ArrayList<>(segments);
    }
}