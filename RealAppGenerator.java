import com.stevesouza.periodicity.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses the REAL project classes to generate LiquiMap images for comparison.
 */
public class RealAppGenerator {

    static final int DAYS_IN_YEAR = 365;
    static final int HOURS_IN_DAY = 24;
    static final int GREEN = new Color(0, 200, 80).getRGB();
    static final int BLACK = Color.BLACK.getRGB();
    static final int SCALE = 3;

    static BufferedImage generate(CompositeJob compositeJob, String label) {
        int hoursInYear = DAYS_IN_YEAR * HOURS_IN_DAY;
        BufferedImage image = new BufferedImage(DAYS_IN_YEAR, HOURS_IN_DAY, BufferedImage.TYPE_INT_RGB);

        for (int currentHour = 0; currentHour < hoursInYear; currentHour++) {
            int x = currentHour / HOURS_IN_DAY;
            int y = currentHour % HOURS_IN_DAY;

            compositeJob.setCurrentHour(currentHour);

            if (compositeJob.isRunning())
                image.setRGB(x, y, GREEN);
            else
                image.setRGB(x, y, BLACK);

            compositeJob.reschedule();
        }

        // Scale up
        int scaledW = DAYS_IN_YEAR * SCALE;
        int scaledH = HOURS_IN_DAY * SCALE;
        int labelHeight = 30;
        BufferedImage scaled = new BufferedImage(scaledW, scaledH + labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, labelHeight, scaledW, scaledH + labelHeight, 0, 0, DAYS_IN_YEAR, HOURS_IN_DAY, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(label, 10, 18);
        g.dispose();
        return scaled;
    }

    public static void main(String[] args) throws Exception {
        // Wedge: schedule=1,4,240 duration=1
        System.out.println("Generating wedge using REAL project classes...");
        CompositeJob wedgeJob = new CompositeJob();
        wedgeJob.add(new StandardJob(
            new PeriodicityClass(new PeriodicityParams(1, 4, 240)),
            new PeriodicityClass(new PeriodicityParams(1))
        ));
        ImageIO.write(generate(wedgeJob, "REAL APP: schedule=1,4,240 duration=1"),
            "PNG", new File("real_wedge.png"));
        System.out.println("Saved: real_wedge.png");

        // Also generate schedule=7 duration=1 for comparison
        CompositeJob diag = new CompositeJob();
        diag.add(new StandardJob(
            new PeriodicityClass(new PeriodicityParams(7)),
            new PeriodicityClass(new PeriodicityParams(1))
        ));
        ImageIO.write(generate(diag, "REAL APP: schedule=7 duration=1"),
            "PNG", new File("real_diag.png"));
        System.out.println("Saved: real_diag.png");

        System.out.println("Done!");
    }
}
