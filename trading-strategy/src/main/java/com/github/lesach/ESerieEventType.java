package com.github.lesach;

public enum ESerieEventType
{
    NULL(-1, "NULL"),
    DerivateSignChangeToDecrease(0, "DerivateSignChangeToDecrease"),
    DerivateSignChangeToIncrease(1, "DerivateSignChangeToIncrease"),
    CrossDown(2, "CrossDown"),
    CrossUp(3, "CrossUp"),
    TimeBefore(4, "TimeBefore"),
    TimeAfter(5, "TimeAfter"),
    TimeBetween(6, "TimeBetween");

    private final int value;
    private final String strValue;

    private ESerieEventType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static ESerieEventType getByValue(int value) {
        ESerieEventType type = null;
        int i = 0;
        ESerieEventType[] values = ESerieEventType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static ESerieEventType getByValue(String value) {
        ESerieEventType type = null;
        int i = 0;
        ESerieEventType[] values = ESerieEventType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }
}
