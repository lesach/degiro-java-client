/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lesach.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author indiketa
 */
public enum DProductType {
    NULL(-1),
    ALL(0),
    SHARES(1),
    BONDS(2),
    FUTURES(7),
    OPTIONS(8),
    INVESTMENT_FUNDS(13),
    LEVERAGED_PRODUCTS(14),
    ETF(131),
    CFD(535),
    WARRANTS(536);

    private final int typeCode;

    DProductType(int typeCode) {
        this.typeCode = typeCode;
    }

    @JsonValue
    public int getTypeCode() {
        return typeCode;
    }

    public static DProductType getProductTypeByValue(int value) {
        DProductType type = null;
        int i = 0;
        DProductType[] values = DProductType.values();
        while (i < values.length && values[i].typeCode != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static DProductType forCode(int code) {
        for (DProductType element : values()) {
            if (element.getTypeCode() == code) {
                return element;
            }
        }
        return null;
    }

    @JsonCreator
    public static DProductType forValue(String v) {
        return DProductType.forCode(Integer.parseInt(v));
    }
}
