package com.github.lesach.strategy.strategy;


public enum EStrategyStepConditionParameterType
{
    NULL(-1,"NULL"),
    Number(1, "Number"),
    Product(2, "Product"),
    Time(3, "Time"),
    Serie(4, "Serie");

    private final int value;
    private final String strValue;

    private EStrategyStepConditionParameterType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static EStrategyStepConditionParameterType getByValue(int value) {
        EStrategyStepConditionParameterType type = null;
        int i = 0;
        EStrategyStepConditionParameterType[] values = EStrategyStepConditionParameterType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static EStrategyStepConditionParameterType getByValue(String value) {
        EStrategyStepConditionParameterType type = null;
        int i = 0;
        EStrategyStepConditionParameterType[] values = EStrategyStepConditionParameterType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }
}