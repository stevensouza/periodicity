package com.stevesouza.periodicity;

class CommandLineArg {

    private String key;
    private String value;

    CommandLineArg(String key, String value) {
        this.key = key;
        this.value = value;
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return
                key + "=" + value;
    }
}
