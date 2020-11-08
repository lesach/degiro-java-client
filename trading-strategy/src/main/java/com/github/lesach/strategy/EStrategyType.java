package com.github.lesach.strategy;

public enum EStrategyType
{
    NULL(-1, "NULL"),
    Put(0, "Put"),
    Call(1, "Call");

    private final int value;
    private final String strValue;

    private EStrategyType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static EStrategyType getByValue(int value) {
        EStrategyType type = null;
        int i = 0;
        EStrategyType[] values = EStrategyType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static EStrategyType getByValue(String value) {
        EStrategyType type = null;
        int i = 0;
        EStrategyType[] values = EStrategyType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }
}
