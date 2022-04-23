package com.stevesouza.surpriseimages;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/** Program that loops through every pixel in a bufferimage and uses various logic to see how to color the pixel.
 * The simple algorithm has surprisingly complex results.
 *
 *   java -classpath .  Paints 0
 *   java -classpath .  Paints 0 10
 *   java -classpath .  Paints 0 43
 *   java -classpath .  Paints 0 58
 *   java -classpath .  Paints 0 37
 *   java -classpath .  Paints 0 13
 *   java -classpath .  Paints 0 97
 *   java -classpath .  Paints 0 123

 *   java -classpath .  Paints 1
 *   java -classpath .  Paints 1 3
 *   java -classpath .  Paints 1 5
 *   java -classpath .  Paints 1 10
 *   java -classpath .  Paints 1 23
 *   java -classpath .  Paints 1 25
 *   java -classpath .  Paints 1 35
 *
 * @author stevesouza - liquilight software
 *
 * Feel free to alter and play with this program.
 *
 * For more info: https://coderanch.com/t/35507/Surprisingly-Complex-Images-Generated-Simple
 *
 */
public class Paints extends JPanel {
    private String[] args;

    public Paints(String[] args) {
        this.args=args;

    }
    static final int WIDTH = 1000, HEIGHT = 1000; // Size of our example

    public String getName() {
        return "Paints";
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    private static final int NOT_PROVIDED=Integer.MAX_VALUE;

    /** Draw the example */
    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        BufferedImage tile =
                new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // get value to use as mod which generates different images
        int modValue;
        if (args==null || args.length<=1)
            modValue=NOT_PROVIDED;
        else
            modValue=Integer.valueOf(args[1]).intValue();

        // loop through every pixel in the buffer using different logic to determine the color of a pixel
        for (int i=0;i<WIDTH;i++)  {
            for (int j=0;j<HEIGHT;j++) {

                if (args==null || args.length==0 || "0".equals(args[0])) { // tangent based coloring
                    if (modValue==NOT_PROVIDED)
                        tile.setRGB(i, j, (int)Math.tan(j*i));
                    else
                        tile.setRGB(i, j, (int)Math.tan(j*i%modValue));
                } else if ("1".equals(args[0])) { // multiplication based coloring
                    if (modValue==NOT_PROVIDED)
                        tile.setRGB(i, j, i*j);
                    else {
                        if (i*i*j*j*3%modValue==0)
                            tile.setRGB(i, j, Color.red.getRGB());
                        else
                            tile.setRGB(i, j, Color.black.getRGB());
                    }
                } else if ("2".equals(args[0]))// simply red.  baseline as red to show that red the loop simply colors every pixel
                    tile.setRGB(i, j, Color.red.getRGB());

            }
        }


        Graphics2D g2d=tile.createGraphics();
        g.drawImage(tile,0,0,WIDTH,HEIGHT,0,0,WIDTH,HEIGHT, null);

    }


    public static void main(String[] args) {
        System.out.println("Sample usage to display surprisingly interesting images:  java -classpath . Paints 0 57");
        System.out.println("java -classpath . Paints 0 57:  0 is or no args base display on tangents. 2nd value if provided is used to create a variety of images by modding with this number");
        System.out.println("java -classpath . Paints 1 21:  1 will base display on multiplication. 2nd value if provided is used to create a variety of images by modding with this number");
        System.out.println("java -classpath . Paints 2:  2 simply colors the screen red, to show that no trickery is creating the diverse images");
        System.out.println();
        System.out.println("Try any of the following as a starting point");
        System.out.println("java -classpath .  Paints 0");
        System.out.println("java -classpath .  Paints 0 10");
        System.out.println("java -classpath .  Paints 0 43");
        System.out.println("java -classpath .  Paints 0 58");
        System.out.println("java -classpath .  Paints 0 37");
        System.out.println("java -classpath .  Paints 0 13");
        System.out.println("java -classpath .  Paints 0 97");
        System.out.println("java -classpath .  Paints 0 123");
        System.out.println();
        System.out.println("java -classpath .  Paints 1");
        System.out.println("java -classpath .  Paints 1 3");
        System.out.println("java -classpath .  Paints 1 5");
        System.out.println("java -classpath .  Paints 1 10");
        System.out.println("java -classpath .  Paints 1 23");
        System.out.println("java -classpath .  Paints 1 25");
        System.out.println("java -classpath .  Paints 1 35");
        System.out.println();
        System.out.println("Feel free to alter and play with this program");

        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setContentPane(new Paints(args));
        f.setSize(WIDTH,HEIGHT);
        f.setVisible(true);
    }
}