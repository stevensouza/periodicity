package periodicity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class CompositeJob implements Job {

    private List jobsList = new ArrayList();

//    public CompositeJob(PeriodicityClass schedule, PeriodicityClass duration) {
//        super(schedule, duration);
//    }

    public void add(Job job) {
        jobsList.add(job);

    }

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
