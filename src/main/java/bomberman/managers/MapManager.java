package bomberman.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MapManager {
    private static final String[] MAP_FILES = {
            "/maps/level1.csv",
            "/maps/level2.csv",
            "/maps/level3.csv",
            "/maps/level4.csv",
            "/maps/level5.csv"
    };
    private String lastMap;
    private List<String> availableMaps;

    public MapManager() {
        resetAvailableMaps();
    }

    /**
     * Reinicia a lista de mapas disponíveis, excluindo o último mapa jogado.
     * Se todos os mapas forem iguais ao último, recarrega todos.
     */
    private void resetAvailableMaps() {
        availableMaps = new ArrayList<>();
        for (String map : MAP_FILES) {
            if (!map.equals(lastMap)) {
                availableMaps.add(map);
            }
        }
        // Caso todos os mapas sejam iguais ao último (ex.: apenas 1 mapa)
        if (availableMaps.isEmpty()) {
            Collections.addAll(availableMaps, MAP_FILES);
        }
    }

    /**
     * Obtém um mapa aleatório que não seja o último jogado.
     * Quando todos os mapas disponíveis forem usados, reinicia o ciclo.
     */
    public String getRandomMap() {
        if (availableMaps.isEmpty()) {
            resetAvailableMaps();
        }

        Random rand = new Random();
        int index = rand.nextInt(availableMaps.size());
        String selectedMap = availableMaps.get(index);
        lastMap = selectedMap;
        availableMaps.remove(index);

        return selectedMap;
    }

    /**
     * Obtém o último mapa selecionado.
     */
    public String getLastMap() {
        return lastMap;
    }
}