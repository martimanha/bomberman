package bomberman.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpriteLoader {
    private static final Map<String, BufferedImage> spriteCache = new HashMap<>();
    private static final BufferedImage missingTexture = createMissingTexture();

    public static BufferedImage loadSprite(String path) {
        if (spriteCache.containsKey(path)) {
            return spriteCache.get(path);
        }

        try {
            BufferedImage sprite = ImageIO.read(SpriteLoader.class.getResourceAsStream(path));
            if (sprite != null) {
                spriteCache.put(path, sprite);
                return sprite;
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erro ao carregar sprite: " + path);
            e.printStackTrace();
        }

        System.err.println("Usando textura substituta para: " + path);
        return missingTexture;
    }

    private static BufferedImage createMissingTexture() {
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                boolean isPink = (x / 8 + y / 8) % 2 == 0;
                img.setRGB(x, y, isPink ? 0xFF00FF : 0x000000);
            }
        }
        return img;
    }

    public static BufferedImage getMissingTexture() {
        return missingTexture;
    }
}