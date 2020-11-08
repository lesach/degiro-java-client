package com.github.lesach.strategy.engine;

public enum EEngineStatus
{
    NULL(-1, "NULL"),
    Starting(1, "Starting"),
    Started(2, "Started"),
    Stopping(3, "Stopping"),
    Stopped(4, "Stopped"),
    Computing(5, "Computing");

    private final int value;
    private final String strValue;

    private EEngineStatus(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

}

