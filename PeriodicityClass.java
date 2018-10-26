package periodicity;

class PeriodicityClass {
//    private double periodicityStart;// the starting point for periodicity.  If something happens every 2 hours this value would be 2.
//    private double periodicityEnd;// the ending point for periodicity.  If something happens every 2 hours this value would be 2.
//    private int periodicityRange;

    private PeriodicityParams params;

    public PeriodicityClass(PeriodicityParams params) {
        this.params = params;
        // this.periodicityStart=this.periodicityEnd=periodicityStart;

    }
//    public PeriodicityClass(double periodicityStart, double periodicityEnd, int periodicityRange) {
//        this.periodicityStart=periodicityStart;
//        this.periodicityEnd=periodicityEnd;
//        this.periodicityRange=periodicityRange;
//    }


    public double getPeriodicity(int currentHour) {
        double periodicity = params.periodicityStart;
        if (params.periodicityStart < params.periodicityEnd) {
            double units = (params.periodicityEnd - params.periodicityStart) / params.periodicityRange;
            periodicity = params.periodicityStart + units * (currentHour % params.periodicityRange);
        } else if (params.periodicityStart > params.periodicityEnd) {
            double units = (params.periodicityStart - params.periodicityEnd) / params.periodicityRange;
            periodicity = params.periodicityStart - units * (currentHour % params.periodicityRange);
        }

        return periodicity;
    }


}
