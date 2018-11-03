package com.stevesouza.periodicity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;

@SpringBootApplication
// note JFrame isn't normally needed for a spring swing/awt app, but in this
// case it is as we are displaying windows.
public class PeriodicityApplication extends JFrame {

    public static void main(String[] args) {
        SpringApplication.run(PeriodicityApplication.class, args);
    }
}