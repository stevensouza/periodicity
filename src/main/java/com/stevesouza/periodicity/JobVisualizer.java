package com.stevesouza.periodicity;

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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * A demonstration of Java2D transformations
 */
public class JobVisualizer extends JPanel {
    int DAYS_IN_YEAR = 365, HOURS_IN_DAY = 24;
    static final int WIDTH = 800, HEIGHT = 50 * 10; // Size of our example


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


    private String argString="";
    private List<CommandLineArg> commandLineArgs = new ArrayList<>();


    private int currentHour;
    private int scale = 2;
    private CompositeJob compositeJob = new CompositeJob();


    public JobVisualizer(String[] args) {
        setCommandLineArgs(args);
    }

    void add(Job job) {
        compositeJob.add(job);
    }

    public String getName() {
        return "Paints";
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        // extra space is due to fact that window size has to include clickable area.
        return numRows+65;
    }

    /**
     * Draw the example
     */
    public void paint(Graphics g1) {
        paintOnce(g1);
    }


    public void paintOnce(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        dispOneYear(image);
        g.scale(scale, scale);
        g.drawImage(image, 0, 0, null);
    }


    public void dispOneYear(BufferedImage image) {
        int x, y; // x=cols, y=rows
        // loop thru hours in the year

        for (currentHour = 0; currentHour < hoursInYear; currentHour++) {
            x = getDay();
            y = getHourOfDay();

            compositeJob.setCurrentHour(currentHour);

            if (compositeJob.isRunning())
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
        return currentHour / numRows;
    }

    private int getHourOfDay() {
        return currentHour % numRows;
    }


    public String getArgString() {
        return argString;
    }

    public void setArgString(String argString) {
        this.argString = argString;
    }


    static JobVisualizer buildJobVisualizer(String[] args) {
        JobVisualizer jobViz = new JobVisualizer(args);
        boolean reverseColor = false;
        Schedule schedule = null;
        Duration duration = null;
        Job job;

        for (CommandLineArg entry : jobViz.getCommandLineArgs()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if ("schedule".equalsIgnoreCase(key) || "s".equalsIgnoreCase(key))
                schedule = new Schedule(PeriodicityParams.create(value));
            else if ("duration".equalsIgnoreCase(key) || "d".equalsIgnoreCase(key))
                duration = new Duration(PeriodicityParams.create(value));
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

    private void setCommandLineArgs(String[] a) {
        for (int i = 0; i < a.length; i++) {
            commandLineArgs.add(new CommandLineArg(name(a[i]), value(a[i])));
        }
    }

    public List<CommandLineArg> getCommandLineArgs() {
        return commandLineArgs;
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


}




