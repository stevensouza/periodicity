package com.stevesouza.periodicity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * CompositeJob is a container for mutliple jobs.  For example it represents when an operating system would have say
 * a backup job scheduled every 12th hour, a virus scan every 15 minutes, and remove old logs every hour.  That
 * would be 3 jobs stored in this composite job. This is an implementation of the Gang of 4 composite design pattern
 */
class CompositeJob implements Job {

    private List<Job> jobsList = new ArrayList<>();

    public void add(Job job) {
        jobsList.add(job);

    }

    /**
     * if any of the contained jobs are running return true
     *
     * @return
     */
    public boolean isRunning() {
        return jobsList.stream().anyMatch(job -> job.isRunning());
    }

    /**
     * Reschedule when each job is supposed to run again
     */
    public void reschedule() {
        jobsList.forEach(Job::reschedule);
    }

    public void setCurrentHour(int currentHour) {
        jobsList.forEach(job->job.setCurrentHour(currentHour));
    }


}
