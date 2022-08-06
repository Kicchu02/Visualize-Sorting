package sorting.visualizer;

import sorting.visualizer.graphics.Picture;
import sorting.visualizer.sorting.MasterSorter;
import sorting.visualizer.sorting.Pixel;
import sorting.visualizer.sorting.SortingMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;

public class Display extends Canvas implements Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String title = "Sorting Visualizer";
    private final JFrame frame;
    private final Picture picture;
    private final BufferedImage image;
    private final int[] pixels;
    private final Pixel[] myPixels;
    private boolean running = false, sorted = false;
    
    public Display() {
        frame = new JFrame();
        
        picture = new Picture("/images/Mona Lisa.jpg");
        
        int width = picture.getWidth();
        int height = picture.getHeight();
        
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        myPixels = new Pixel[width * height];
        
        Dimension size = new Dimension(400, (int) (400.0 / width * height));
        this.setPreferredSize(size);
        
        initPixels();
    }
    
    public static void main(String[] args) {
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setVisible(true);
        
        display.start();
    }
    
    private void initPixels() {
        for (int i = 0; i < pixels.length; i++) {
            myPixels[i] = new Pixel(picture.getPixels()[i], i);
        }
        randomizePixels();
    }
    
    private void randomizePixels() {
        ArrayList<Pixel> pixelList = new ArrayList<>();
        
        Collections.addAll(pixelList, myPixels);
        
        Collections.shuffle(pixelList);
        
        for (int i = 0; i < myPixels.length; i++) {
            myPixels[i] = pixelList.get(i);
        }
    }
    
    public synchronized void start() {
        running = true;
        Thread thread = new Thread(this, "Display");
        thread.start();
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 400;
        double delta = 0;
        int frames = 0;
        
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
            }
            render();
            frames++;
            
            if (System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
                frame.setTitle(title + " | " + frames + " fps");
                frames = 0;
            }
        }
        
    }
    
    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = myPixels[i].color;
        }
        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }
    
    private void update() {
        if (!sorted) {
            sorted = MasterSorter.sort(myPixels, SortingMethod.BubbleSort, 50000);
        }
    }
    
}
