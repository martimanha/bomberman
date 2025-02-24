package bomberman.managers;

import bomberman.entities.Enemy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
                            enemies.add(new Enemy(col, row, enemies)); // Passa a lista de referência
                            map[row][col] = 'V'; // Substitui por vazio após criar inimigo
                            break;
                        case 'P':
                            if (playerX != -1) throw new Exception("Posição duplicada do jogador");
                            playerX = col;
                            playerY = row;
                            map[row][col] = 'V'; // Remove marcador do jogador
                            break;
                        case ' ':
                            map[row][col] = 'V'; // Normaliza espaços
                            break;
                    }
                }
                row++;
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