package com.github.lesach.strategy;

import java.util.List;

public enum EIndicatorType
{
    Last(1, "Last"),
    Bid(2, "Bid"),
    Ask(3, "Ask"),
    ComputationResult(4, "ComputationResult"),
    ADL(37, "Accumulation/Distribution"),
    ADX(36, "Average Directional Index"),
    Aroon(35, "Aroon"),
    ATR(34, "Average True Range"),
    BollingerBand(33, "BollingerBand"),
    CCI(32, "Commodity Channel Index"),
    CMF(31, "Chaikin Money Flow"),
    CMO(30, "Chande Momentum Oscillator"),
    DEMA(29, "BigDecimal Exponential Moving Average"),
    DPO(28, "Detrended Price Oscillator"),
    EMA(11, "Exponential Moving Average"),
    Envelope(12, "Moving Average Envelopes"),
    Ichimoku(13, "Ichimoku Clouds"),
    MACD(14, "Moving Average Convergence Divergence"),
    Momentum(15, "Momentum"),
    OBV(16, "On Balance Volume"),
    PVT(17, "Price Volume Trend"),
    ROC(18, "Rate of Change"),
    RSI(19, "Relative Strength Index"),
    SAR(20, "Stop and Reverse"),
    SMA(21, "Simple Moving Average"),
    TRIX(22, "TRIX"),
    Volume(23, "Volume"),
    VROC(24, "Volume Rate of Change"),
    WMA(25, "Weighted Moving Average"),
    WPR(26, "Williams %R"),
    ZLEMA(27, "Zero Lag EMA"),
    None(100, "None");
    
    private final int value;
    private final String strValue;

    EIndicatorType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static EIndicatorType getByValue(int value) {
        EIndicatorType type = null;
        int i = 0;
        EIndicatorType[] values = EIndicatorType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static EIndicatorType getByValue(String value) {
        EIndicatorType type = null;
        int i = 0;
        EIndicatorType[] values = EIndicatorType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

}
