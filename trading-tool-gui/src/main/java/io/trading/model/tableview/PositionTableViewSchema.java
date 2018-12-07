package io.trading.model.tableview;


import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

public class PositionTableViewSchema extends BasicSchema{
    private final SimpleStringProperty id;
    private final SimpleStringProperty place;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty dailyPL;
    private final SimpleDoubleProperty dailyVariation;
    private final SimpleDoubleProperty totalPL;
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
                                    double price,
                                    long quantity,
                                    String currency,
                                    double total,
                                    double dailyPL,
                                    double dailyVariation,
                                    double totalPL,
                                    String time) {
        super();
        this.setProductId(id);
        this.setProductName(product);
        this.setCurrency(currency);
        this.setQuantity(quantity);
        this.id = new SimpleStringProperty(id);
        this.place = new SimpleStringProperty(place);
        this.price = new SimpleDoubleProperty(price);
        this.setTotal(total);
        this.dailyPL = new SimpleDoubleProperty(dailyPL);
        this.dailyVariation = new SimpleDoubleProperty(dailyVariation);
        this.totalPL = new SimpleDoubleProperty(totalPL);
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
                       double price,
                       long quantity,
                       String currency,
                       double total,
                       double dailyPL,
                       double dailyVariation,
                       double totalPL,
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
     * Update prices
     * @param ask
     * @param bid
     */
    public void update(double ask,
                       double bid) {
        this.setAsk(ask);
        this.setBid(bid);
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

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
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
