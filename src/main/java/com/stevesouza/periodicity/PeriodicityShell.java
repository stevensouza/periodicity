package com.stevesouza.periodicity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.swing.*;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.stream.Collectors;


@ShellComponent
public class PeriodicityShell {

    // @Value("${file-name}")

    // variables that track where the window will open.  The nubmers are incremented after
    // each windows is opened so they don't overlay each other.
    private static int x = 10;
    private static int y = 10;
    private static int X_INCREMENT = 10;
    private static int Y_INCREMENT = 20;

    private Map<String, JobVisualizerHolder> visualizers = new HashMap<>();
    private int jobVisualizerCounter;

    private String[] toArray(String arg) {
        String cleaned = arg.replace(" ", "");
        // split on number,alpha. example: s=1,d=2,4,55 would split on
        //   s=1
        //   d=2,4,55
        return cleaned.split(";");
    }


    @ShellMethod(key = {"r", "run"}, value = "Run the periodicity program.")
    public void displayWindow(
            @ShellOption(
                    value = {"-a", "-args", "--a", "--args"},
                    help = "Example: schedule=7,11,duration=1,2",
                    defaultValue = "schedule=7;duration=1")
                    String argString) {
        String[] args = toArray(argString);
        JFrame f = new JFrame();
        JobVisualizer jobVisualizer = JobVisualizer.buildJobVisualizer(args);
        jobVisualizer.setArgString(argString);
        jobVisualizerCounter++;

        visualizers.put(String.valueOf(jobVisualizerCounter), new JobVisualizerHolder(jobVisualizer, f));
        f.setTitle(jobVisualizer.getArgString());
        f.setContentPane(jobVisualizer);
        f.setSize(jobVisualizer.getWidth(), jobVisualizer.getHeight());
        Rectangle r = new Rectangle(x, y, jobVisualizer.getWidth(), jobVisualizer.getHeight());
        x += X_INCREMENT;
        y += Y_INCREMENT;
        f.setBounds(r);
        f.setVisible(true);
    }



    @ShellMethod(key = {"h"}, value = "Further help on how ot run program.")
    public void help() {
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


    @ShellMethod(key = {"shutdown","s"}, value = "Close windows and exit the application")
    public void shutDownApp() {
        System.exit(0);
    }

    @ShellMethod(key = {"list","l"}, value = "List the windows created this session")
    public List<String> list() {
        // change str, str to str;str
        // return entries of the format: 1 - [schedule=7;duration=1]
        return  visualizers.entrySet().stream()
                .map(entry->{return entry.getKey()+" - "+entry.getValue().getJobVisualizer().getArgString();})
                .collect(Collectors.toList());
    }

    @ShellMethod(key = {"open","o"}, value = "Open a previously run window based on its id")
    public void open(@ShellOption(
            value = {"-id","-i", "--id", "--i"},
            help = "id of window as returned by 'list'")
            String id) {
        JobVisualizerHolder holder = visualizers.get(id);
        displayWindow(holder.getJobVisualizer().getArgString());
    }

    @ShellMethod(key = {"combine","c"}, value = "Combine any numbers of images based on their id as returned from 'list': combine 1,5,7")
    public void combine(@ShellOption(
            value = {"-ids","-i", "--ids", "--i"},
            help = "id or id's of windows as returned by 'list'")
                             String ids) {

        List<String> args = Arrays.asList(ids.replaceAll(" ", "").split(","));

        // get list that only contains keys that are passed in
        List<JobVisualizerHolder> holder = visualizers
                .entrySet()
                .stream()
                .filter(e->(args.contains(e.getKey())))
                .map(e-> {return e.getValue();})
                .collect(Collectors.toList());

        // combine the arguments passed to each JobVisualizer instance to create a string that effectively combines the image
        //  (simulates the user typing in all strings for 1 image)
        String combineString = holder
                .stream()
                .map(jobVisualizerHolder ->
                    {return jobVisualizerHolder.getJobVisualizer().getArgString();})
                .collect(Collectors.joining(";"));

        displayWindow(combineString);

    }

    @ShellMethod(key = {"reset"}, value = "Close all open windows")
    public void reset() {
        visualizers.values().forEach(h->h.getJFrame().dispatchEvent(new WindowEvent(h.getJFrame(), WindowEvent.WINDOW_CLOSING)));
        visualizers.clear();
    }

    @Data
    @AllArgsConstructor
   private static class JobVisualizerHolder {
        private JobVisualizer jobVisualizer;
        private JFrame jFrame;
    }

}
