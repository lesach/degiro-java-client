/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lesach.client;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author indiketa
 */
public enum DOrderAction {
    NULL(-1, "N"),
    BUY(0, "B"),
    SELL(1, "S");

    private final int value;
    private final String strValue;

    private DOrderAction(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    @JsonValue
    public String getStrValue() {
        return strValue;
    }

    public static DOrderAction getOrderByValue(int value) {
        DOrderAction type = null;
        int i = 0;
        DOrderAction[] values = DOrderAction.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static DOrderAction getOrderByValue(String value) {
        DOrderAction type = null;
        int i = 0;
        DOrderAction[] values = DOrderAction.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

}
