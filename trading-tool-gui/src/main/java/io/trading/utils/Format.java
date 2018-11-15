package io.trading.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Format {
    static DecimalFormat bigDecimalFormat = new DecimalFormat("#,##0.00");

    /**
     * Return a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static String formatBigDecimal(BigDecimal d) {
        if (d == null)
            return "";
        return bigDecimalFormat.format(d.doubleValue());
    }
}
