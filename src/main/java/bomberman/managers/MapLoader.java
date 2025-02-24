package bomberman.managers;

import bomberman.entities.Enemy;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    private static boolean isValidSpecialTile(int x, int y, char[][] map, int playerX, int playerY) {
        // 1. Verificar bordas excluindo cantos
        boolean isEdge = (x == 0 || x == MAP_COLS-1 || y == 0 || y == MAP_ROWS-1);
        boolean isCorner = (x == 0 && y == 0) ||
                (x == 0 && y == MAP_ROWS-1) ||
                (x == MAP_COLS-1 && y == 0) ||
                (x == MAP_COLS-1 && y == MAP_ROWS-1);
        if (!isEdge || isCorner) return false;

        // 2. Distância mínima de 4 tiles do jogador
        if (Math.abs(x - playerX) < 4 || Math.abs(y - playerY) < 4) return false;

        // 3. Verificar parede DESTRUTÍVEL (B) do lado oposto
        if (x == 0 && map[y][x+1] != 'B') return false;          // Esquerda
        if (x == MAP_COLS-1 && map[y][x-1] != 'B') return false; // Direita
        if (y == 0 && map[y+1][x] != 'B') return false;          // Topo
        if (y == MAP_ROWS-1 && map[y-1][x] != 'B') return false; // Base

        // 4. Só gerar em paredes indestrutíveis (H)
        return map[y][x] == 'H';
    }

    private static void addSpecialTile(char[][] map, int playerX, int playerY) {
        List<int[]> validPositions = new ArrayList<>();
        Random rand = new Random();

        // Coletar posições válidas
        for (int y = 0; y < MAP_ROWS; y++) {
            for (int x = 0; x < MAP_COLS; x++) {
                if (isValidSpecialTile(x, y, map, playerX, playerY)) {
                    validPositions.add(new int[]{x, y});
                }
            }
        }

        if (!validPositions.isEmpty()) {
            int[] pos = validPositions.get(rand.nextInt(validPositions.size()));
            map[pos[1]][pos[0]] = 'S';
        } else {
            createForcedPath(map, playerX, playerY);
        }
    }

    private static void createForcedPath(char[][] map, int startX, int startY) {
        int[][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}};

        for (int[] dir : directions) {
            int x = startX;
            int y = startY;
            List<int[]> path = new ArrayList<>();

            while (true) {
                x += dir[0];
                y += dir[1];

                if (x < 0 || x >= MAP_COLS || y < 0 || y >= MAP_ROWS) {
                    // Colocar 'S' na última posição válida
                    if (!path.isEmpty()) {
                        int[] lastPos = path.get(path.size()-1);
                        map[lastPos[1]][lastPos[0]] = 'S';
                        // Garantir parede destrutível oposta
                        int oppositeX = lastPos[0] - dir[0];
                        int oppositeY = lastPos[1] - dir[1];
                        if (oppositeX >= 0 && oppositeX < MAP_COLS &&
                                oppositeY >= 0 && oppositeY < MAP_ROWS) {
                            map[oppositeY][oppositeX] = 'B';
                        }
                    }
                    return;
                }

                // Converter paredes indestrutíveis em destrutíveis
                if (map[y][x] == 'H') map[y][x] = 'B';
                path.add(new int[]{x, y});
            }
        }
    }

    private static boolean hasPathToS(char[][] map, int startX, int startY, int endX, int endY) {
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        boolean[][] visited = new boolean[MAP_ROWS][MAP_COLS];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == endX && current[1] == endY) return true;

            for (int[] dir : dirs) {
                int newX = current[0] + dir[0];
                int newY = current[1] + dir[1];

                if (newX >= 0 && newX < MAP_COLS &&
                        newY >= 0 && newY < MAP_ROWS &&
                        !visited[newY][newX] &&
                        map[newY][newX] != 'H') { // Considerar apenas paredes destrutíveis como caminho válido

                    visited[newY][newX] = true;
                    queue.add(new int[]{newX, newY});
                }
            }
        }
        return false;
    }

    private static void ensureAccessiblePath(char[][] map, int startX, int startY) {
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        boolean[][] visited = new boolean[MAP_ROWS][MAP_COLS];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (!isBorder(x, y)) {
                if (map[y][x] == 'H') {
                    map[y][x] = 'B'; // Converter para destrutível
                }
                return;
            }

            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                if (newX >= 0 && newX < MAP_COLS &&
                        newY >= 0 && newY < MAP_ROWS &&
                        !visited[newY][newX] &&
                        map[newY][newX] != 'H') { // Ignorar paredes indestrutíveis

                    visited[newY][newX] = true;
                    queue.add(new int[]{newX, newY});
                }
            }
        }
    }

    private static boolean isBorder(int x, int y) {
        return x == 0 || x == MAP_COLS - 1 || y == 0 || y == MAP_ROWS - 1;
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

                for (int col = 0; col < MAP_COLS; col++) {
                    char tile = tiles[col].trim().charAt(0);
                    map[row][col] = tile;

                    switch (tile) {
                        case 'E':
                            enemies.add(new Enemy(col, row, enemies));
                            map[row][col] = 'V';
                            break;
                        case 'P':
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

            addSpecialTile(map, playerX, playerY);
            ensureAccessiblePath(map, playerX, playerY);

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