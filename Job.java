package periodicity;

interface Job {

    public boolean isRunning();

    public void setCurrentHour(int currentHour);

    //   public void setSchedule(PeriodicityClass schedule);
    //   public PeriodicityClass getSchedule();
    //  public void setDuration(PeriodicityClass duration);
    //  public PeriodicityClass getDuration();
    public void reschedule();


}
