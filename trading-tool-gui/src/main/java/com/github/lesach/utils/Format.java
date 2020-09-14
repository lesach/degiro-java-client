package com.github.lesach.utils;

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
     * Parse a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static BigDecimal parseBigDecimal(String d) {
        if (d == null)
            return null;
        try {
            return new BigDecimal(bigDecimalFormat.parse(d).doubleValue());
        }
        catch (Exception e) {
            return null;
        }
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

    /**
     * Return a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static String formatDate(long d) {
        return dateFormat.format(new Date(d));
    }

    /**
     * Return a formatted BigDecimal
     * @param d number
     * @return String
     */
    public static Date parseDate(String d) {
        try {
            return dateFormat.parse(d);
        }
        catch (Exception e) {
            return null;
        }
    }
}
