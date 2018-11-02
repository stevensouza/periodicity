package com.stevesouza.periodicity;

class StandardJob implements Job {

    private int currentHour;
    private double nextStartHour, nextEndHour;
    private Schedule schedule;
    private Duration duration;

    private boolean isRunning, prevRunning = false;


    public StandardJob(Schedule schedule, Duration duration) {
        this.schedule = schedule;
        this.duration = duration;
        nextStartHour = 0;
        nextEndHour = nextStartHour + getDurationValue();
    }

    public boolean isRunning() {
        prevRunning = isRunning;
        if (currentHour >= nextStartHour && currentHour < nextEndHour)
            isRunning = true;
        else
            isRunning = false;

        return isRunning;
    }


    public boolean prevRunning() {
        return prevRunning;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;

    }


    public void reschedule() {
        if (!isRunning && prevRunning) {
            nextStartHour += schedule.getPeriodicity(currentHour);
            nextEndHour = nextStartHour + getDurationValue();
            //     System.out.println("next="+nextStartHour+", end="+nextEndHour+", current="+currentHour);

        }

    }


    protected double getDurationValue() {
        return Math.ceil(duration.getPeriodicity(currentHour));
    }

}
