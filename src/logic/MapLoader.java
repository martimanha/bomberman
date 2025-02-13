package src.logic;

import src.entities.Enemy;
import src.entities.Player;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MapLoader {
    public static int[][] loadMap(String mapName, List<Enemy> enemies, Player player) {
        List<String> lines = new ArrayList<>();
        String path = "src/resources/maps/" + mapName; // Caminho corrigido
        
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar o mapa: " + path);
            e.printStackTrace();
            return null;
        }

        int[][] map = new int[lines.size()][];
        for (int y = 0; y < lines.size(); y++) {
            String[] cells = lines.get(y).split(",");
            map[y] = new int[cells.length];
            
            for (int x = 0; x < cells.length; x++) {
                switch (cells[x].trim()) {
                    case "H": map[y][x] = 1; break;  // Parede
                    case "B": map[y][x] = 2; break;  // Bloco destrutível
                    case "P":                       // Jogador
                        map[y][x] = 0;
                        player.setPosition(x * 32, y * 32);
                        break;
                    case "E":                       // Inimigo
                        map[y][x] = 0;
                        enemies.add(new Enemy(x * 32, y * 32));
                        break;
                    default:  map[y][x] = 0; break; // Espaço vazio
                }
            }
        }
        return map;
    }
}