package com.github.lesach;

public enum EBooleanOperator
{
    NULL(-1, "NONE"),
    OR(0, "OR"),
    AND(1, "AND"),
    XOR(2, "XOR");


    private final int value;
    private final String strValue;

    private EBooleanOperator(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static EBooleanOperator getByValue(int value) {
        EBooleanOperator type = null;
        int i = 0;
        EBooleanOperator[] values = EBooleanOperator.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static EBooleanOperator getByValue(String value) {
        EBooleanOperator type = null;
        int i = 0;
        EBooleanOperator[] values = EBooleanOperator.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }
}
