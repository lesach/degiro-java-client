package io.trading.model.tableview;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class PositionTableViewSchema {
    private final SimpleStringProperty product;
    private final SimpleStringProperty place;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty quantity;
    private final SimpleStringProperty currency;
    private final SimpleDoubleProperty total;
    private final SimpleDoubleProperty dailyPL;
    private final SimpleDoubleProperty dailyVariation;
    private final SimpleDoubleProperty totalPL;
    private final SimpleStringProperty time;

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
    private PositionTableViewSchema(String product,
                                    String place,
                                    double price,
                                    double quantity,
                                    String currency,
                                    double total,
                                    double dailyPL,
                                    double dailyVariation,
                                    double totalPL,
                                    String time) {
        this.product = new SimpleStringProperty(product);
        this.place = new SimpleStringProperty(place);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.currency = new SimpleStringProperty(currency);
        this.total = new SimpleDoubleProperty(total);
        this.dailyPL = new SimpleDoubleProperty(dailyPL);
        this.dailyVariation = new SimpleDoubleProperty(dailyVariation);
        this.totalPL = new SimpleDoubleProperty(totalPL);
        this.time = new SimpleStringProperty(time);
    }


    public String getProduct() {
        return product.get();
    }

    public SimpleStringProperty productProperty() {
        return product;
    }

    public void setProduct(String product) {
        this.product.set(product);
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

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getQuantity() {
        return quantity.get();
    }

    public SimpleDoubleProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    public String getCurrency() {
        return currency.get();
    }

    public SimpleStringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public double getTotal() {
        return total.get();
    }

    public SimpleDoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public double getDailyPL() {
        return dailyPL.get();
    }

    public SimpleDoubleProperty dailyPLProperty() {
        return dailyPL;
    }

    public void setDailyPL(double dailyPL) {
        this.dailyPL.set(dailyPL);
    }

    public double getDailyVariation() {
        return dailyVariation.get();
    }

    public SimpleDoubleProperty dailyVariationProperty() {
        return dailyVariation;
    }

    public void setDailyVariation(double dailyVariation) {
        this.dailyVariation.set(dailyVariation);
    }

    public double getTotalPL() {
        return totalPL.get();
    }

    public SimpleDoubleProperty totalPLProperty() {
        return totalPL;
    }

    public void setTotalPL(double totalPL) {
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
}
