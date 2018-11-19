package io.trading.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
    private static DecimalFormat bigDecimalFormat = new DecimalFormat("#,##0.00");

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    /**
     * Return a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static String formatBigDecimal(Double d) {
        if (d == null)
            return "";
        return bigDecimalFormat.format(d);
    }

    /**
     * Return a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static String formatDate(Date d) {
        if (d == null)
            return "";
        return dateFormat.format(d);
    }
}
