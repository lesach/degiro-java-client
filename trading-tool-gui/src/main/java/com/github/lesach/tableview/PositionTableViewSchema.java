package com.github.lesach.tableview;


import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

import java.math.BigDecimal;

public class PositionTableViewSchema extends BasicSchema{
    private final SimpleStringProperty id;
    private final SimpleStringProperty place;
    private final ObjectProperty<BigDecimal> price;
    private final ObjectProperty<BigDecimal> dailyPL;
    private final ObjectProperty<BigDecimal> dailyVariation;
    private final ObjectProperty<BigDecimal> totalPL;
    private final SimpleStringProperty time;
    private final SimpleStringProperty DummySell;
    private final SimpleStringProperty DummyStopLoss;

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
        this.id = new SimpleStringProperty(id);
        this.place = new SimpleStringProperty(place);
        this.price = new SimpleObjectProperty<BigDecimal>(price);
        this.setTotal(total);
        this.dailyPL = new SimpleObjectProperty<BigDecimal>(dailyPL);
        this.dailyVariation = new SimpleObjectProperty<BigDecimal>(dailyVariation);
        this.totalPL = new SimpleObjectProperty<BigDecimal>(totalPL);
        this.time = new SimpleStringProperty(time);
        this.DummySell = new SimpleStringProperty(null);
        this.DummyStopLoss = new SimpleStringProperty(null);
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

    /**
     * Callback to make the GUI able to detect a item update
     * @return
     */
    public static Callback<PositionTableViewSchema, Observable[]> extractor() {
        return (PositionTableViewSchema p) -> new Observable[]{
            p.idProperty(),
            p.productNameProperty(),
            p.placeProperty(),
            p.priceProperty(),
            p.quantityProperty(),
            p.currencyProperty(),
            p.totalProperty(),
            p.dailyPLProperty(),
            p.dailyVariationProperty(),
            p.totalPLProperty(),
            p.timeProperty()
        };
    }

    public String getPlace() {
        return place.get();
    }

    public SimpleStringProperty placeProperty() {
        return place;
    }

    public void setPlace(String place) {
        this.place.set(place);
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public BigDecimal getDailyPL() {
        return dailyPL.get();
    }

    public ObjectProperty<BigDecimal> dailyPLProperty() {
        return dailyPL;
    }

    public void setDailyPL(BigDecimal dailyPL) {
        this.dailyPL.set(dailyPL);
    }

    public BigDecimal getDailyVariation() {
        return dailyVariation.get();
    }

    public ObjectProperty<BigDecimal> dailyVariationProperty() {
        return dailyVariation;
    }

    public void setDailyVariation(BigDecimal dailyVariation) {
        this.dailyVariation.set(dailyVariation);
    }

    public BigDecimal getTotalPL() {
        return totalPL.get();
    }

    public ObjectProperty<BigDecimal> totalPLProperty() {
        return totalPL;
    }

    public void setTotalPL(BigDecimal totalPL) {
        this.totalPL.set(totalPL);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getDummySell() {
        return DummySell.get();
    }

    public SimpleStringProperty dummySellProperty() {
        return DummySell;
    }

    public void setDummySell(String dummySell) {
        this.DummySell.set(dummySell);
    }

    public String getDummyStopLoss() {
        return DummyStopLoss.get();
    }

    public SimpleStringProperty dummyStopLossProperty() {
        return DummyStopLoss;
    }

    public void setDummyStopLoss(String dummyStopLoss) {
        this.DummyStopLoss.set(dummyStopLoss);
    }

}
