package com.stevesouza.periodicity;

class PeriodicityClass {

    private PeriodicityParams params;

    public PeriodicityClass(PeriodicityParams params) {
        this.params = params;
    }

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
