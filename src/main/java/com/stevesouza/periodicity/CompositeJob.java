package com.stevesouza.periodicity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** CompositeJob is a container for mutliple jobs.  For example it represents when an operating system would have say
 * a backup job scheduled every 12th hour, a virus scan every 15 minutes, and remove old logs every hour.  That
 * would be 3 jobs stored in this composite job. This is an implementation of the Gang of 4 composite design pattern
 *
 */
class CompositeJob implements Job {

    private List jobsList = new ArrayList();


    public void add(Job job) {
        jobsList.add(job);

    }

    /**
     * if any of the contained jobs are running return true
     * @return
     */
    public boolean isRunning() {
        Iterator iter = jobsList.iterator();
        boolean isRunning = false;
        while (iter.hasNext()) {
            Job job = (Job) iter.next();
            if (job.isRunning())
                isRunning = true;
        }

        return isRunning;
    }

    /** Reschedule when each job is supposed to run again
     *
     */
    public void reschedule() {
        Iterator iter = jobsList.iterator();
        while (iter.hasNext()) {
            Job job = (Job) iter.next();
            job.reschedule();
        }

    }

    public void setCurrentHour(int currentHour) {
        Iterator iter = jobsList.iterator();
        while (iter.hasNext()) {
            Job job = (Job) iter.next();
            job.setCurrentHour(currentHour);
        }

    }


}
