package com.stevesouza.periodicity;

interface Job {
    public boolean isRunning();

    public void setCurrentHour(int currentHour);

    public void reschedule();
}
