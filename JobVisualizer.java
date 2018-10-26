package periodicity;

/*
 * Copyright (c) 2000 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 2nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book (recommended),
 * visit http://www.davidflanagan.com/javaexamples2.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A demonstration of Java2D transformations
 */
public class JobVisualizer extends JPanel {
    int DAYS_IN_YEAR = 365, HOURS_IN_DAY = 24;
    static final int WIDTH = 800, HEIGHT = 50 * 10; // Size of our example
//  static final int WIDTH = DAYS_IN_YEAR, HEIGHT = HOURS_IN_DAY; // Size of our example


    private static int BLUE = 223482;
    // periodicityStart and periodicityEnd interact in the following way.   periodicity means how often in hours the job will be scheduled.
    // 2 would means it is scheduled every 2 hours.   periodicityStart is the value that it starts out with and it cycles to the value
    // of periodicityEnd at the end of the periodicityRange.  For example if periodicityRange is 48 (hours) and periodictyStart is 2 and
    // periodicityEnd is 4 hours then after each scheduled job is run then the periodicity will increase by
    // (4-2)/48

    private int numRows = HOURS_IN_DAY;
    private int hoursInYear = DAYS_IN_YEAR * numRows;
    private int runColor = BLUE;
    private int noRunColor = Color.black.getRGB();

    private int currentHour;
    private int scale = 2;
    private CompositeJob compositeJob = new CompositeJob();

    public JobVisualizer() {

        // pass in
        //  scale size
        //  multiple jobs
        //  (job = periodicity, duration or schedule/duration)
        // reverse color
        // runcolor, noruncolor
        // duration1=

    }

    void add(Job job) {
        compositeJob.add(job);
    }

////      next add
////          duplicate logic for periodicity for duration
////          add ability to pass in parameters 
////          add multiple parameters for combined runs.  if blue in all cases then run.  if blue in any case then run. (and/or)
////          also instead of scheduling and running. always schedule say 30 minutes ofter a run, or is that what it is doing?


    public String getName() {
        return "Paints";
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    /**
     * Draw the example
     */
    public void paint(Graphics g1) {
        paintOnce(g1);
    }


    public void paintOnce(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        dispOneYear(image);
        g.scale(scale, scale);
        g.drawImage(image, 0, 0, null);

    }


    public void dispOneYear(BufferedImage image) {


        int x, y; // x=cols, y=rows
        // loop thru hours in the year
        boolean isRunning;

        for (currentHour = 0; currentHour < hoursInYear; currentHour++) {
            x = getDay();
            y = getHourOfDay();

            compositeJob.setCurrentHour(currentHour);
            isRunning = compositeJob.isRunning();

            if (isRunning)
                image.setRGB(x, y, runColor);
            else
                image.setRGB(x, y, noRunColor);

            compositeJob.reschedule();


        }
    }


    private void reverseColor() {
        int color = runColor;
        runColor = noRunColor;
        noRunColor = color;
    }


    private int getDay() {
        int day = currentHour / numRows;
        return day;
    }

    private int getHourOfDay() {
//      int hourOfDay=currentHour%HOURS_IN_DAY;
        int hourOfDay = currentHour % numRows;
        return hourOfDay;
    }

    private static JobVisualizer buildJobVisualizer(List paramsList) {

        Iterator iter = paramsList.iterator();

        JobVisualizer jobViz = new JobVisualizer();
        boolean reverseColor = false;
        Schedule schedule = null;
        Duration duration = null;
        Job job;

        while (iter.hasNext()) {
            CommandLineArg entry = (CommandLineArg) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();

            if ("schedule".equalsIgnoreCase(key))
                schedule = new Schedule(PeriodicityParams.init(value));
            else if ("duration".equalsIgnoreCase(key))
                duration = new Duration(PeriodicityParams.init(value));
            else if ("runcolor".equalsIgnoreCase(key))
                jobViz.runColor = Integer.parseInt(value);
            else if ("noruncolor".equalsIgnoreCase(key))
                jobViz.noRunColor = Integer.parseInt(value);
            else if ("reversecolor".equalsIgnoreCase(key))
                reverseColor = true;
            else if ("scale".equalsIgnoreCase(key))
                jobViz.scale = Integer.parseInt(value);
            else if ("numrows".equalsIgnoreCase(key)) {
                jobViz.numRows = Integer.parseInt(value);
            }

            jobViz.hoursInYear = jobViz.DAYS_IN_YEAR * jobViz.numRows;

            if (schedule != null && duration != null) {
                job = new StandardJob(schedule, duration);
                jobViz.add(job);

                job = null;
                schedule = null;
                duration = null;
            }


        }

        if (reverseColor)
            jobViz.reverseColor();

        return jobViz;


    }

    private static List argsToList(String[] a) {
        List list = new ArrayList();
        for (int i = 0; i < a.length; i++) {
            list.add(new CommandLineArg(name(a[i]), value(a[i])));
        }

        return list;


    }

    private static String name(String param) {
        String[] arr = param.split("=");
        return arr[0] == null ? "" : arr[0].trim();
    }

    private static String value(String param) {
        String[] arr = param.split("=");
        String name = name(param);
        String value = (arr[1] == null) ? "" : arr[1].trim();

        return value;

    }


    private static void help() {
        System.out.println("Property of LiquiLight Software LLC:");
        System.out.println("This program will performs modulo logic to see what regualarly scheduled jobs may look like.");
        System.out.println("Rows are the 24 hours of the day and columns are the 365 days of the year.");
        System.out.println("Sample calls follow:");
        System.out.println(" java -cp periodcity.jar periodicity.JobVisualizer schedule=1,4,240 duration=1");
        System.out.println(" java -cp periodcity.jar periodicity.JobVisualizer schedule=7 duration=1 schedule=5 duration=2,4,480");
        System.out.println(" java -cp periodcity.jar periodicity.JobVisualizer schedule=13 duration=1 (Default)");
        System.out.println("schedule represents the periodicity in hours for when a job runs.  The above indicates the job will start running every 1 hour,");
        System.out.println("the over a 240 hour period linearly move to every 4 hours. When schedule is one number it will run the job every nth hour");
        System.out.println("In the second case above it would run every 7th hour.  Duration represents how long the scheduled job will run in hours and works similarly");
        System.out.println("to schedule.  The first 2 values in schedule and duration can be floats and the third value has to be an integer.");
        System.out.println("Any number of schedules may be added.  They must be paired (i.e. every schedule must have a specified duration). ");
        System.out.println("Other variables that can be used are runcolor=rgbvalue (ex. runcolor=9999999), noruncolor=rgbvalue.  rgbcolor defaults to blue, ");
        System.out.println("and noruncolor defaults to black. reversecolor=yes/true/1 indicates to reverse the colors.");
        System.out.println("scale=integer (ex. scale=2 is the default) indicates how large to make the pixels.  ");
        System.out.println("numRows=integer (ex. numRows=24 is the default) indicates the number of rows and so will effect the way the periodicity looks. ");

    }

    public static void main(String[] args) {


        if (args == null || args.length == 0) {
            args = new String[]{"schedule=13", "duration=1"};
            help();
        }

        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        f.setContentPane(buildJobVisualizer(argsToList(args)));
        f.setSize(WIDTH, HEIGHT);
        f.setVisible(true);
    }
}




