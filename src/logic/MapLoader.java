package src.logic;

import java.util.Random;
public class MapLoader {
    private static String lastMap = "";

    public static String loadRandomMap(String mode){
        String[] mapFiles;

        if(mode.equals("LABRINTO")){
            mapFiles = new String[]{
                "NormalMap1.csv",
                "NormalMap2.csv",
                "NormalMap3.csv",
                "NormalMap4.csv",
                "NormalMap5.csv",
                "LabyrinthMap1.csv",
                "LabyrinthMap2.csv",
                "LabyrinthMap3.csv"
                
            };
        } else{
            mapFiles = new String[]{
                "NormalMap1.csv",
                "NormalMap2.csv",
                "NormalMap3.csv",
                "NormalMap4.csv",
                "NormalMap5.csv"
            };
        }

        return selectRandomMap(mapFiles);
    } 

    private static String selectRandomMap(String[] mapFiles){
        Random random = new Random();
        String selectedMap;

        do{
            selectedMap = mapFiles[random.nextInt(mapFiles.length)];
        } while(selectedMap.equals(lastMap));
        lastMap = selectedMap;
        return selectedMap;
    }

    public static char[][] loadMap(String mapFile){

        //alterar para ler o mapa
        return new char[0][0];
    }
}
