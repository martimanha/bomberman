package src.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapLoader {

    private static final List <String> NORMAL_MAPS = List.of("","","","","");

    private static final List <String> LABIRINTO_MAPS = List.of("","","");

    private static final Random random = new Random();
    private static String lastMAP = null; //Vai armazenar o ultimo mapa que foi carregado.

    public static String loadMap(String modo) 
    {
        List<String> availableMaps = new ArrayList<>();

        if (modo.equalsIgnoreCase("Labirinto"))
        {
            //No modo labirinto, vai haver mapas normais e mapas de labirinto.

            availableMaps.addAll(LABIRINTO_MAPS);
                availableMaps.addAll(NORMAL_MAPS);
        }

        //Remove o ultimo mapa carregado da lista de mapas disponiveis.
        if (lastMAP != null && availableMaps.contains(lastMAP)) {
            availableMaps.remove(lastMAP);
        }

        //Seleciona um mapa aleatorio da lista de mapas disponiveis.
        String selectedMap = availableMaps.get(random.nextInt(availableMaps.size()));

        //Atualiza o ultimo mapa carregado.
        lastMAP = selectedMap;

        return selectedMap;
    }
}