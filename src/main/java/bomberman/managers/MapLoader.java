package bomberman.managers;

import bomberman.entities.Enemy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapLoader {
    public static final int MAP_COLS = 30;
    public static final int MAP_ROWS = 20;

    public static class LoadResult {
        public char[][] map;
        public List<Enemy> enemies;
        public int playerX;
        public int playerY;

        public LoadResult(char[][] map, List<Enemy> enemies, int playerX, int playerY) {
            this.map = map;
            this.enemies = enemies;
            this.playerX = playerX;
            this.playerY = playerY;
        }
    }

    private static boolean isBorderTile(int x, int y) {
        return x == 0 || x == MAP_COLS - 1 || y == 0 || y == MAP_ROWS - 1;
    }

    private static boolean isFarFromPlayer(int x, int y, int playerX, int playerY) {
        int dx = Math.abs(x - playerX);
        int dy = Math.abs(y - playerY);
        return (dx + dy) > 8;
    }

    public static LoadResult loadMap(String filePath) {
        List<Enemy> enemies = new ArrayList<>();
        int playerX = -1;
        int playerY = -1;

        try (InputStream is = MapLoader.class.getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            char[][] map = new char[MAP_ROWS][MAP_COLS];
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < MAP_ROWS) {
                String[] tiles = line.trim().split(",", MAP_COLS);

                if (tiles.length != MAP_COLS) {
                    throw new Exception("Linha inválida: " + (row + 1) + " - Esperadas " + MAP_COLS + " colunas");
                }

                for (int col = 0; col < MAP_COLS; col++) {
                    char tile = tiles[col].trim().charAt(0);
                    map[row][col] = tile;

                    switch (tile) {
                        case 'E':
                            enemies.add(new Enemy(col, row, enemies));
                            map[row][col] = 'V';
                            break;
                        case 'P':
                            if (playerX != -1) throw new Exception("Posição duplicada do jogador");
                            playerX = col;
                            playerY = row;
                            map[row][col] = 'V';
                            break;
                        case ' ':
                            map[row][col] = 'V';
                            break;
                    }
                }
                row++;
            }

            List<Point> possibleSTiles = new ArrayList<>();
            for (int y = 0; y < MAP_ROWS; y++) {
                for (int x = 0; x < MAP_COLS; x++) {
                    if (map[y][x] == 'H' && isBorderTile(x, y) && isFarFromPlayer(x, y, playerX, playerY)) {
                        possibleSTiles.add(new Point(x, y));
                    }
                }
            }

            if (!possibleSTiles.isEmpty()) {
                Random rand = new Random();
                Point sPos = possibleSTiles.get(rand.nextInt(possibleSTiles.size()));
                map[sPos.y][sPos.x] = 'S';
            }

            if (row != MAP_ROWS) {
                throw new Exception("Número de linhas inválido: " + row + "/" + MAP_ROWS);
            }

            if (playerX == -1) {
                throw new Exception("Posição do jogador não encontrada");
            }

            return new LoadResult(map, enemies, playerX, playerY);

        } catch (Exception e) {
            System.err.println("Erro ao carregar mapa: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}