import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Headless generator for the Paints "surprise images" program.
 * Replicates the core logic from Paints.java to generate PNGs without a display.
 */
public class PaintsGenerator {

    static final int WIDTH = 800, HEIGHT = 800;

    static BufferedImage generateTangent(int modValue, String label) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (modValue == -1)
                    image.setRGB(i, j, (int) Math.tan(j * i));
                else
                    image.setRGB(i, j, (int) Math.tan(j * i % modValue));
            }
        }
        return addLabel(image, label);
    }

    static BufferedImage generateMultiplication(int modValue, String label) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (modValue == -1) {
                    image.setRGB(i, j, i * j);
                } else {
                    if ((long)i * i * j * j * 3 % modValue == 0)
                        image.setRGB(i, j, Color.red.getRGB());
                    else
                        image.setRGB(i, j, Color.black.getRGB());
                }
            }
        }
        return addLabel(image, label);
    }

    // Bonus: some additional interesting formulas
    static BufferedImage generateSin(int modValue, String label) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                int val = (int)(Math.sin(i * j % modValue) * 0xFFFFFF);
                image.setRGB(i, j, val);
            }
        }
        return addLabel(image, label);
    }

    static BufferedImage generateXor(int modValue, String label) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                int val = (i ^ j) % modValue;
                int rgb = new Color(
                    Math.abs(val * 7) % 256,
                    Math.abs(val * 3) % 256,
                    Math.abs(val * 11) % 256
                ).getRGB();
                image.setRGB(i, j, rgb);
            }
        }
        return addLabel(image, label);
    }

    static BufferedImage generateAtan2(int modValue, String label) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        int cx = WIDTH / 2, cy = HEIGHT / 2;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                double angle = Math.atan2(j - cy, i - cx);
                double dist = Math.sqrt((i - cx) * (i - cx) + (j - cy) * (j - cy));
                int val = (int)(Math.tan(angle * modValue + dist / 10));
                image.setRGB(i, j, val);
            }
        }
        return addLabel(image, label);
    }

    static BufferedImage addLabel(BufferedImage image, String label) {
        int labelHeight = 30;
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight() + labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, result.getWidth(), labelHeight);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(label, 10, 20);
        g.drawImage(image, 0, labelHeight, null);
        g.dispose();
        return result;
    }

    public static void main(String[] args) throws Exception {
        new File("visualizations/paints").mkdirs();
        int index = 1;

        // === Algorithm 0: Tangent-based ===
        System.out.println("Generating tangent-based patterns...");

        // No mod - raw tangent
        save(generateTangent(-1, "tan(j*i) - raw tangent, no modulo"), "paints_" + (index++) + ".png");

        // Various mod values from the original comments
        int[] tanMods = {10, 13, 37, 43, 57, 58, 97, 123};
        for (int mod : tanMods) {
            save(generateTangent(mod, "tan(j*i % " + mod + ")"), "paints_" + (index++) + ".png");
        }

        // === Algorithm 1: Multiplication-based ===
        System.out.println("Generating multiplication-based patterns...");

        // No mod - raw i*j
        save(generateMultiplication(-1, "i*j - raw multiplication color"), "paints_" + (index++) + ".png");

        // Various mod values
        int[] mulMods = {3, 5, 10, 23, 25, 35};
        for (int mod : mulMods) {
            save(generateMultiplication(mod, "i*i*j*j*3 % " + mod + " == 0 (red/black)"), "paints_" + (index++) + ".png");
        }

        // === Bonus: Additional interesting formulas ===
        System.out.println("Generating bonus patterns...");

        save(generateSin(57, "sin(i*j % 57) - sine variation"), "paints_" + (index++) + ".png");
        save(generateXor(64, "XOR: (i^j) % 64 - Sierpinski-like"), "paints_" + (index++) + ".png");
        save(generateXor(256, "XOR: (i^j) % 256 - fractal carpet"), "paints_" + (index++) + ".png");
        save(generateAtan2(8, "atan2 + tan spiral (mod=8)"), "paints_" + (index++) + ".png");
        save(generateAtan2(23, "atan2 + tan spiral (mod=23)"), "paints_" + (index++) + ".png");

        System.out.println("Done! Generated " + (index - 1) + " Paints images.");
    }

    static void save(BufferedImage img, String filename) throws Exception {
        ImageIO.write(img, "PNG", new File("visualizations/paints/" + filename));
        System.out.println("  Saved: visualizations/paints/" + filename);
    }
}
