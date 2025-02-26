package bomberman.managers;

import static bomberman.GameConstants.*;

import bomberman.entities.Enemy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapManager {
    private final List<String> allMaps;
    private List<String> availableMaps;
    private final List<String> playedMaps = new ArrayList<>();
    private static final Random random = new Random();

    public MapManager() {
        allMaps = new ArrayList<>();
        allMaps.add("/maps/level1.csv");
        allMaps.add("/maps/level2.csv");
        allMaps.add("/maps/level3.csv");
        allMaps.add("/maps/level4.csv");
        allMaps.add("/maps/level5.csv");
        resetMaps();
    }

    public String getRandomMap() {
        if (availableMaps.isEmpty()) {
            resetMaps();
        }
        return availableMaps.remove(0);
    }

    public void resetMaps() {
        availableMaps = new ArrayList<>(allMaps);
        Collections.shuffle(availableMaps);
        playedMaps.clear();
    }

    public int getTotalMaps() {
        return allMaps.size();
    }

    public int getRemainingMaps() {
        return availableMaps.size();
    }

    private static boolean hasPathToS(char[][] map, int startX, int startY, int endX, int endY) {
        boolean[][] visited = new boolean[MAP_ROWS][MAP_COLS];
        List<int[]> queue = new ArrayList<>();
        queue.add(new int[]{startX, startY});
        visited[startY][startX] = true;

        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}};

        while (!queue.isEmpty()) {
            int[] current = queue.remove(0);
            if (current[0] == endX && current[1] == endY) return true;

            for (int[] dir : directions) {
                int newX = current[0] + dir[0];
                int newY = current[1] + dir[1];

                if (newX >= 0 && newX < MAP_COLS &&
                        newY >= 0 && newY < MAP_ROWS &&
                        !visited[newY][newX] &&
                        map[newY][newX] != 'H') {

                    visited[newY][newX] = true;
                    queue.add(new int[]{newX, newY});
                }
            }
        }
        return false;
    }

    private static void addSpecialTile(char[][] map, int playerX, int playerY) {
        List<int[]> validPositions = new ArrayList<>();

        for (int y = 0; y < MAP_ROWS; y++) {
            for (int x = 0; x < MAP_COLS; x++) {
                if (isValidSpecialTile(x, y, map, playerX, playerY)) {
                    validPositions.add(new int[]{x, y});
                }
            }
        }

        if (!validPositions.isEmpty()) {
            int[] pos = validPositions.get(random.nextInt(validPositions.size()));
            map[pos[1]][pos[0]] = 'S';
        } else {
            createForcedPath(map, playerX, playerY);
        }
    }

    private static boolean isValidSpecialTile(int x, int y, char[][] map, int playerX, int playerY) {
        if (Math.abs(x - playerX) < 6 || Math.abs(y - playerY) < 6) return false;

        if (map[y][x] != 'H') return false;

        boolean hasDestructibleNeighbor = false;
        int[][] neighbors = {{x+1,y}, {x-1,y}, {x,y+1}, {x,y-1}};
        for (int[] n : neighbors) {
            if (n[0] >= 0 && n[0] < MAP_COLS &&
                    n[1] >= 0 && n[1] < MAP_ROWS) {
                if (map[n[1]][n[0]] == 'B') {
                    hasDestructibleNeighbor = true;
                    break;
                }
            }
        }
        return hasDestructibleNeighbor;
    }

    private static void createForcedPath(char[][] map, int startX, int startY) {
        for (int y = 0; y < MAP_ROWS; y++) {
            for (int x = 0; x < MAP_COLS; x++) {
                if (map[y][x] == 'H' && hasPathToS(map, startX, startY, x, y)) {
                    map[y][x] = 'B';
                }
            }
        }
    }
}