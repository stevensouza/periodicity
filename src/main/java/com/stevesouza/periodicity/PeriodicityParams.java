package com.stevesouza.periodicity;

class PeriodicityParams {
    double periodicityStart;// the starting point for periodicity.  If something happens every 2 hours this value would be 2.
    double periodicityEnd;// the ending point for periodicity.  If something happens every 2 hours this value would be 2.
    int periodicityRange;

    public PeriodicityParams(double periodicityStart) {
        this.periodicityStart = this.periodicityEnd = periodicityStart;

    }

    public PeriodicityParams(double periodicityStart, double periodicityEnd, int periodicityRange) {
        this.periodicityStart = periodicityStart;
        this.periodicityEnd = periodicityEnd;
        this.periodicityRange = periodicityRange;
    }


    static PeriodicityParams init(String params) {
        if (params.contains(","))
            return init(params.split(","));
        else
            return new PeriodicityParams(Double.parseDouble(params.trim()));

    }

    // 4,24,480 (typically the last value is some range such as in this case 24*20 (20 days))
    private static PeriodicityParams init(String[] paramsArr) {

        double start = Double.parseDouble(paramsArr[0].trim());
        double end = Double.parseDouble(paramsArr[1].trim());
        int range = Integer.parseInt(paramsArr[2].trim());

        return new PeriodicityParams(start, end, range);
    }


}
