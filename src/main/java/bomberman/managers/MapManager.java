package bomberman.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapManager {
    private final List<String> allMaps;
    private List<String> availableMaps;
    private final List<String> playedMaps = new ArrayList<>();
    private final Random random = new Random();

    public MapManager() {
        // Inicializa com todos os mapas
        allMaps = new ArrayList<>();
        allMaps.add("/maps/level1.csv");
        allMaps.add("/maps/level2.csv");
        allMaps.add("/maps/level3.csv");
        allMaps.add("/maps/level4.csv");
        allMaps.add("/maps/level5.csv");

        resetMaps(); // Prepara a primeira rodada
    }

    public String getRandomMap() {
        if (availableMaps.isEmpty()) {
            resetMaps();
        }

        String selectedMap = availableMaps.remove(0);
        playedMaps.add(selectedMap);
        return selectedMap;
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
}