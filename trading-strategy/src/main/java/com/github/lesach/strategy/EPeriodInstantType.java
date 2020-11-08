package com.github.lesach.strategy;

public enum EPeriodInstantType {
    NULL(-1, "NULL"),
    Start(0, "Start"),
    Stop(1, "Stop"),
    Continue(2, "Continue");

    private final int value;
    private final String strValue;

    private EPeriodInstantType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static EPeriodInstantType getByValue(int value) {
        EPeriodInstantType type = null;
        int i = 0;
        EPeriodInstantType[] values = EPeriodInstantType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static EPeriodInstantType getByValue(String value) {
        EPeriodInstantType type = null;
        int i = 0;
        EPeriodInstantType[] values = EPeriodInstantType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }
}
