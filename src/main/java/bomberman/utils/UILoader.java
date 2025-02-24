package bomberman.utils;

import java.awt.image.BufferedImage;

public class UILoader {
    public static BufferedImage loadUISprite(String path) {
        String fullPath = "/ui/" + path;
        BufferedImage sprite = SpriteLoader.loadSprite(fullPath);

        if (sprite == null) {
            System.err.println("Falha ao carregar UI: " + fullPath);
        }
        return sprite;
    }
}