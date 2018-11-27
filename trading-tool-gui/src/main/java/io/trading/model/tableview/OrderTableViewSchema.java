package io.trading.model.tableview;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

public class OrderTableViewSchema {
    private final SimpleStringProperty id;
    private final SimpleStringProperty buyOrSell;
    private final SimpleStringProperty product;
    private final SimpleStringProperty orderType;
    private final SimpleDoubleProperty limit;
    private final SimpleStringProperty currency;
    private final SimpleLongProperty quantity;
    private final SimpleDoubleProperty ask;
    private final SimpleDoubleProperty bid;
    private final SimpleStringProperty DUMMY;
    private final SimpleBooleanProperty error;

    /**
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param limit
     * @param currency
     * @param quantity
     */
    public OrderTableViewSchema(String id,
                                String buyOrSell,
                                String product,
                                String orderType,
                                double limit,
                                String currency,
                                long quantity) {
        this.id = new SimpleStringProperty(id);
        this.buyOrSell = new SimpleStringProperty(buyOrSell);
        this.product = new SimpleStringProperty(product);
        this.orderType = new SimpleStringProperty(orderType);
        this.limit = new SimpleDoubleProperty(limit);
        this.currency = new SimpleStringProperty(currency);
        this.quantity = new SimpleLongProperty(quantity);
        this.ask = new SimpleDoubleProperty(0.0d);
        this.bid = new SimpleDoubleProperty(0.0d);
        this.DUMMY = new SimpleStringProperty(null);
        this.error = new SimpleBooleanProperty(false);
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
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param limit
     * @param currency
     * @param quantity
     */
    public void update (String id,
                        String buyOrSell,
                        String product,
                        String orderType,
                        double limit,
                        String currency,
                        long quantity) {
        this.setId(id);
        this.setBuyOrSell(buyOrSell);
        this.setProduct(product);
        this.setOrderType(orderType);
        this.setLimit(limit);
        this.setCurrency(currency);
        this.setQuantity(quantity);
    }


    /**
     * Callback to make the GUI able to detect a item update
     * @return
     */
    public static Callback<OrderTableViewSchema, Observable[]> extractor() {
        return (OrderTableViewSchema p) -> new Observable[]{
                p.idProperty(),
                p.buyOrSellProperty(),
                p.productProperty(),
                p.orderTypeProperty(),
                p.limitProperty(),
                p.currencyProperty(),
                p.quantityProperty()
        };
    }

    public String getBuyOrSell() {
        return buyOrSell.get();
    }

    public SimpleStringProperty buyOrSellProperty() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell.set(buyOrSell);
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

    public String getOrderType() {
        return orderType.get();
    }

    public SimpleStringProperty orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.set(orderType);
    }

    public double getLimit() {
        return limit.get();
    }

    public SimpleDoubleProperty limitProperty() {
        return limit;
    }

    public void setLimit(double price) {
        this.limit.set(price);
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

    public long getQuantity() {
        return quantity.get();
    }

    public SimpleLongProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity.set(quantity);
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

    public double getAsk() {
        return ask.get();
    }

    public SimpleDoubleProperty askProperty() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask.set(ask);
    }

    public double getBid() {
        return bid.get();
    }

    public SimpleDoubleProperty bidProperty() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid.set(bid);
    }

    public String getDUMMY() {
        return DUMMY.get();
    }

    public SimpleStringProperty DUMMYProperty() {
        return DUMMY;
    }

    public void setDUMMY(String DUMMY) {
        this.DUMMY.set(DUMMY);
    }

    public boolean isError() {
        return error.get();
    }

    public SimpleBooleanProperty errorProperty() {
        return error;
    }

    public void setError(boolean error) {
        this.error.set(error);
    }

}
