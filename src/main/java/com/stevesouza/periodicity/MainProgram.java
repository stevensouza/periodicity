package com.stevesouza.periodicity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainProgram {

    // variables that track where the window will open.  The nubmers are incremented after
    // each windows is opened so they don't overlay each other.
    private static int x = 10;
    private static int y = 10;
    private static int X_INCREMENT = 10;
    private static int Y_INCREMENT = 20;

    private static JFrame displayWindow(String[] args) {
        JFrame f = new JFrame();
        JobVisualizer jobVisualizer = JobVisualizer.buildJobVisualizer(args);
        f.setTitle(jobVisualizer.getCommandLineArgs().toString());
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        f.setContentPane(jobVisualizer);
        f.setSize(jobVisualizer.getWidth(), jobVisualizer.getHeight());
        Rectangle r = new Rectangle(x, y, jobVisualizer.getWidth(), jobVisualizer.getHeight());
        x += X_INCREMENT;
        y += Y_INCREMENT;
        f.setBounds(r);
        f.setVisible(true);

        return f;
    }

    private void help() {
        System.out.println("Property of LiquiLight Software LLC:");
        System.out.println("This program will performs modulo logic to see what regualarly scheduled jobs may look like.");
        System.out.println("Rows are the 24 hours of the day and columns are the 365 days of the year.");
        System.out.println();
        System.out.println("Sample calls follow:");
        System.out.println(" java -cp periodcity.jar com.stevesouza.periodicity.MainProgram schedule=1,4,240 duration=1");
        System.out.println(" java -cp periodcity.jar com.stevesouza.periodicity.MainProgram schedule=7 duration=1 schedule=5 duration=2,4,480");
        System.out.println(" java -cp periodcity.jar com.stevesouza.MainProgram schedule=13 duration=1 (Default)");
        System.out.println();
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
        MainProgram program = new MainProgram();

        if (args == null || args.length == 0) {
            args = new String[]{"schedule=13", "duration=1"};
            program.help();
        }

        /**
         * This is a Swing program.  JobVisualizer inherits from and is a JPanel/canvas that is written too within a JFrame/Window.
         * JobVisualizer's paint method is called to render the screen.
         *
         * info on JFrame:
         *
         * With these few instructions we will obtain a window which can be maximize, minimize, change it´s size with the mouse,
         * etc. When we create a JFrame object we start an engine which manages the user interface. This engine communicates
         * with the operative system both to paint in the screen as to receive information from the keyboard and from the mouse.
         * We will call this engine "AWT Engine" or "Swing Engine"
         *
         * JPanel: The canvas
         *
         * To be able to paint we want to know WHERE and where is an JPanel object which will be included in the window.
         * We extend the JPanel class to be able to overwrite the paint method which is the method called by the AWT Engine
         * to paint what appears in the screen.
         */

        // for (int i = 0; i < 1; i++) {
        JFrame f = program.displayWindow(args);
        //}

    }

}
