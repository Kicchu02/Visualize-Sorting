package sorting.visualizer.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Picture {
    
    private final String path;
    private int[] pixels;
    private BufferedImage image;
    
    public Picture(String path) {
        this.path = path;
        load();
    }
    
    private void load() {
        try {
            image = ImageIO.read(Objects.requireNonNull(Picture.class.getResource(path)));
            int w = image.getWidth();
            int h = image.getHeight();
            pixels = new int[w * h];
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public int[] getPixels() {
        return pixels;
    }
    
    public int getWidth() {
        return image.getWidth();
    }
    
    public int getHeight() {
        return image.getHeight();
    }
    
}
