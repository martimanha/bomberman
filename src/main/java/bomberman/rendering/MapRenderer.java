package bomberman.rendering;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import static bomberman.GameConstants.TILE_SIZE;

public class MapRenderer {

    public static void render(Graphics2D g2, char[][] map,
                              BufferedImage floorSprite, BufferedImage[] blockSprites) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                renderTile(g2, x, y, map[y][x], floorSprite, blockSprites);
            }
        }
    }

    private static void renderTile(Graphics2D g2, int x, int y, char tile,
                                   BufferedImage floorSprite, BufferedImage[] blockSprites) {
        int posX = x * TILE_SIZE;
        int posY = y * TILE_SIZE;

        // Renderizar chão primeiro
        g2.drawImage(floorSprite, posX, posY, TILE_SIZE, TILE_SIZE, null);

        // Renderizar blocos por cima
        switch(tile) {
            case 'H': // Parede indestrutível
                g2.drawImage(blockSprites[0], posX, posY, TILE_SIZE, TILE_SIZE, null);
                break;

            case 'B': // Parede destrutível
            case 'S': // Bloco especial (mesmo sprite que destrutível)
                g2.drawImage(blockSprites[1], posX, posY, TILE_SIZE, TILE_SIZE, null);
                break;
        }
    }
}