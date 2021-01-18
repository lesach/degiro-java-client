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
public enum DOrderType {
    NULL(-1, "NULL"),
    LIMIT(0, "LIMIT"),
    MARKET(2, "MARKET"),
    STOPLOSS(3, "STOPLOSS"),
    STOPLIMIT(1, "STOPLIMIT"),
    TRAILINGSTOP(4, "TRAILINGSTOP"),
    STANDARDAMOUNT(5, "STANDARDAMOUNT"),
    STANDARDSIZE(6,"STANDARDSIZE");

    private final int value;
    private final String strValue;

    private DOrderType(int value, String strValue) {
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

    public static DOrderType getOrderByValue(int value) {
        DOrderType type = null;
        int i = 0;
        DOrderType[] values = DOrderType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static DOrderType getOrderByValue(String value) {
        DOrderType type = null;
        int i = 0;
        DOrderType[] values = DOrderType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

}
