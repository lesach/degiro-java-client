package com.github.lesach.webapp.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PositionTableViewSchema extends BasicSchema {
    private String id;
    private String place;
    private BigDecimal price;
    private BigDecimal dailyPL;
    private BigDecimal dailyVariation;
    private BigDecimal totalPL;
    private String time;
    private String DummySell;
    private String DummyStopLoss;

    /**
     *
     * @param product
     * @param place
     * @param price
     * @param quantity
     * @param currency
     * @param total
     * @param dailyPL
     * @param dailyVariation
     * @param quantity
     * @param totalPL
     * @param time
     */
    public PositionTableViewSchema(String id,
                                    String product,
                                    String place,
                                    BigDecimal price,
                                    long quantity,
                                    String currency,
                                    BigDecimal total,
                                    BigDecimal dailyPL,
                                    BigDecimal dailyVariation,
                                    BigDecimal totalPL,
                                    String time) {
        super();
        this.setProductId(id);
        this.setProductName(product);
        this.setCurrency(currency);
        this.setQuantity(quantity);
        this.id = id;
        this.place = place;
        this.price = price;
        this.setTotal(total);
        this.dailyPL = dailyPL;
        this.dailyVariation = dailyVariation;
        this.totalPL = totalPL;
        this.time = time;
        this.DummySell = null;
        this.DummyStopLoss = null;
    }


    /**
     *
     * @param product
     * @param place
     * @param price
     * @param quantity
     * @param currency
     * @param total
     * @param dailyPL
     * @param dailyVariation
     * @param quantity
     * @param totalPL
     * @param time
     */
    public void update(String id,
                       String product,
                       String place,
                       BigDecimal price,
                       long quantity,
                       String currency,
                       BigDecimal total,
                       BigDecimal dailyPL,
                       BigDecimal dailyVariation,
                       BigDecimal totalPL,
                       String time) {
        this.setId(id);
        this.setProductName(product);
        this.setPlace(place);
        this.setPrice (price);
        this.setQuantity(quantity);
        this.setCurrency (currency);
        this.setTotal(total);
        this.setDailyPL(dailyPL);
        this.setDailyVariation(dailyVariation);
        this.setTotalPL(totalPL);
        this.setTime(time);
    }
}
