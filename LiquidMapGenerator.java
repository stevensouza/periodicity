import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Standalone headless generator for liquid map visualizations.
 * Replicates the core logic from the periodicity project to generate PNG images.
 */
public class LiquidMapGenerator {

    static final int DAYS_IN_YEAR = 365;
    static final int HOURS_IN_DAY = 24;
    static final int GREEN = new Color(0, 200, 80).getRGB();
    static final int SCALE = 3;

    // --- Core classes inlined ---

    static class PeriodicityParams {
        double start, end;
        int range;
        PeriodicityParams(double start) { this.start = this.end = start; }
        PeriodicityParams(double start, double end, int range) {
            this.start = start; this.end = end; this.range = range;
        }
    }

    static class Periodicity {
        PeriodicityParams p;
        Periodicity(PeriodicityParams p) { this.p = p; }
        double get(int currentHour) {
            double val = p.start;
            if (p.start < p.end) {
                double units = (p.end - p.start) / p.range;
                val = p.start + units * (currentHour % p.range);
            } else if (p.start > p.end) {
                double units = (p.start - p.end) / p.range;
                val = p.start - units * (currentHour % p.range);
            }
            return val;
        }
    }

    static class StandardJob {
        int currentHour;
        double nextStartHour, nextEndHour;
        Periodicity schedule, duration;
        boolean isRunning, prevRunning;

        StandardJob(Periodicity schedule, Periodicity duration) {
            this.schedule = schedule;
            this.duration = duration;
            nextStartHour = 0;
            nextEndHour = Math.ceil(duration.get(0));
        }

        boolean checkRunning() {
            prevRunning = isRunning;
            isRunning = currentHour >= nextStartHour && currentHour < nextEndHour;
            return isRunning;
        }

        void reschedule() {
            if (!isRunning && prevRunning) {
                nextStartHour += schedule.get(currentHour);
                nextEndHour = nextStartHour + Math.ceil(duration.get(currentHour));
                // Re-check: the new window may include the current hour
                isRunning = currentHour >= nextStartHour && currentHour < nextEndHour;
            }
            // Advance past any windows we've completely missed
            while (currentHour >= nextEndHour) {
                double oldStart = nextStartHour;
                nextStartHour += schedule.get(currentHour);
                nextEndHour = nextStartHour + Math.ceil(duration.get(currentHour));
                if (nextStartHour <= oldStart) break; // safety valve
                isRunning = currentHour >= nextStartHour && currentHour < nextEndHour;
            }
        }
    }

    static BufferedImage generate(List<StandardJob> jobs, int numRows, int runColor, int noRunColor, String label) {
        int hoursInYear = DAYS_IN_YEAR * numRows;
        int numCols = DAYS_IN_YEAR;

        BufferedImage image = new BufferedImage(numCols, numRows, BufferedImage.TYPE_INT_RGB);

        for (int hour = 0; hour < hoursInYear; hour++) {
            int x = hour / numRows;
            int y = hour % numRows;

            for (StandardJob job : jobs) {
                job.currentHour = hour;
            }

            boolean anyRunning = false;
            for (StandardJob job : jobs) {
                if (job.checkRunning()) anyRunning = true;
            }

            image.setRGB(x, y, anyRunning ? runColor : noRunColor);

            for (StandardJob job : jobs) {
                job.reschedule();
            }
        }

        // Scale up the image
        int scaledW = numCols * SCALE;
        int scaledH = numRows * SCALE;

        // Add space for label
        int labelHeight = 30;
        BufferedImage scaled = new BufferedImage(scaledW, scaledH + labelHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // Draw scaled image
        g.drawImage(image, 0, labelHeight, scaledW, scaledH + labelHeight, 0, 0, numCols, numRows, null);

        // Draw label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString(label, 10, 18);

        // Draw axis labels
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(new Color(100, 100, 100));
        g.drawString("Days ->", scaledW / 2 - 20, labelHeight - 3);

        g.dispose();
        return scaled;
    }

    static BufferedImage stackImages(List<BufferedImage> images, List<String> titles) {
        int totalHeight = 0;
        int maxWidth = 0;
        int padding = 15;

        for (BufferedImage img : images) {
            totalHeight += img.getHeight() + padding;
            maxWidth = Math.max(maxWidth, img.getWidth());
        }

        BufferedImage result = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, maxWidth, totalHeight);

        int yOffset = 0;
        for (int i = 0; i < images.size(); i++) {
            g.drawImage(images.get(i), 0, yOffset, null);
            yOffset += images.get(i).getHeight() + padding;
        }

        g.dispose();
        return result;
    }

    public static void main(String[] args) throws Exception {
        List<BufferedImage> allImages = new ArrayList<>();
        int green = GREEN;
        int black = Color.BLACK.getRGB();

        // === 1. Simple fixed periods for reference ===
        System.out.println("Generating: Fixed period every 7 hours...");
        List<StandardJob> jobs1 = new ArrayList<>();
        jobs1.add(new StandardJob(
            new Periodicity(new PeriodicityParams(7)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs1, 24, green, black, "1) Every 7 hours, 1hr duration - diagonal drift"));

        // === 2. Every 13 hours - steeper diagonal ===
        System.out.println("Generating: Fixed period every 13 hours...");
        List<StandardJob> jobs2 = new ArrayList<>();
        jobs2.add(new StandardJob(
            new Periodicity(new PeriodicityParams(13)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs2, 24, green, black, "2) Every 13 hours, 1hr duration - different diagonal"));

        // === 3. WEDGE: Linear periodicity transition ===
        System.out.println("Generating: Wedge pattern (schedule 1->4 over 240 hours)...");
        List<StandardJob> jobs3 = new ArrayList<>();
        jobs3.add(new StandardJob(
            new Periodicity(new PeriodicityParams(1, 4, 240)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs3, 24, green, black, "3) WEDGE: schedule=1,4,240 duration=1 - spreading pattern"));

        // === 4. WEDGE variant: schedule 2->8 over 480 hours ===
        System.out.println("Generating: Wedge pattern (schedule 2->8 over 480 hours)...");
        List<StandardJob> jobs4 = new ArrayList<>();
        jobs4.add(new StandardJob(
            new Periodicity(new PeriodicityParams(2, 8, 480)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs4, 24, green, black, "4) WEDGE: schedule=2,8,480 duration=1 - wider spread"));

        // === 5. Growing duration - job stretches over time ===
        System.out.println("Generating: Growing duration (1->7 hours over 9600 hours)...");
        List<StandardJob> jobs5 = new ArrayList<>();
        jobs5.add(new StandardJob(
            new Periodicity(new PeriodicityParams(7)),
            new Periodicity(new PeriodicityParams(1, 7, 8760))
        ));
        allImages.add(generate(jobs5, 24, green, black, "5) schedule=7 duration=1,7,8760 - job grows over the year"));

        // === 6. The rescheduling scenario: fixed gap after finish + growing duration ===
        System.out.println("Generating: Reschedule 6hrs after finish, growing duration...");
        List<StandardJob> jobs6 = new ArrayList<>();
        jobs6.add(new StandardJob(
            new Periodicity(new PeriodicityParams(6)),
            new Periodicity(new PeriodicityParams(1, 5, 4380))
        ));
        allImages.add(generate(jobs6, 24, green, black, "6) schedule=6 duration=1,5,4380 - reschedule after finish, growing"));

        // === 7. Composite: two jobs with different periods (interference) ===
        System.out.println("Generating: Two overlapping jobs (period 7 + period 11)...");
        List<StandardJob> jobs7 = new ArrayList<>();
        jobs7.add(new StandardJob(
            new Periodicity(new PeriodicityParams(7)),
            new Periodicity(new PeriodicityParams(1))
        ));
        jobs7.add(new StandardJob(
            new Periodicity(new PeriodicityParams(11)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs7, 24, new Color(180, 50, 50).getRGB(), black,
            "7) Composite: schedule=7,d=1 + schedule=11,d=1 - interference"));

        // === 8. Composite: three jobs (OS-like workload) ===
        System.out.println("Generating: OS workload (3 jobs)...");
        List<StandardJob> jobs8 = new ArrayList<>();
        jobs8.add(new StandardJob(
            new Periodicity(new PeriodicityParams(12)),
            new Periodicity(new PeriodicityParams(2))
        ));
        jobs8.add(new StandardJob(
            new Periodicity(new PeriodicityParams(5)),
            new Periodicity(new PeriodicityParams(1))
        ));
        jobs8.add(new StandardJob(
            new Periodicity(new PeriodicityParams(24)),
            new Periodicity(new PeriodicityParams(3))
        ));
        allImages.add(generate(jobs8, 24, new Color(50, 150, 80).getRGB(), black,
            "8) OS workload: 3 jobs (every 12h/2h, 5h/1h, 24h/3h)"));

        // === 9. Schedule shrinking: starts slow, gets faster ===
        System.out.println("Generating: Schedule shrinking (12->2 over 480 hours)...");
        List<StandardJob> jobs9 = new ArrayList<>();
        jobs9.add(new StandardJob(
            new Periodicity(new PeriodicityParams(12, 2, 480)),
            new Periodicity(new PeriodicityParams(1))
        ));
        allImages.add(generate(jobs9, 24, new Color(200, 150, 50).getRGB(), black,
            "9) REVERSE WEDGE: schedule=12,2,480 - accelerating job"));

        // === 10. Both schedule AND duration changing ===
        System.out.println("Generating: Both schedule and duration changing...");
        List<StandardJob> jobs10 = new ArrayList<>();
        jobs10.add(new StandardJob(
            new Periodicity(new PeriodicityParams(7, 11, 240)),
            new Periodicity(new PeriodicityParams(1, 4, 480))
        ));
        allImages.add(generate(jobs10, 24, new Color(130, 80, 180).getRGB(), black,
            "10) schedule=7,11,240 duration=1,4,480 - both changing"));

        // Save individual images
        for (int i = 0; i < allImages.size(); i++) {
            String filename = "liquid_map_" + (i + 1) + ".png";
            ImageIO.write(allImages.get(i), "PNG", new File(filename));
            System.out.println("Saved: " + filename);
        }

        // Save combined image
        BufferedImage combined = stackImages(allImages, new ArrayList<>());
        ImageIO.write(combined, "PNG", new File("liquid_maps_all.png"));
        System.out.println("Saved combined: liquid_maps_all.png");

        System.out.println("\nDone! Generated " + allImages.size() + " liquid map visualizations.");
    }
}
