package io.trading.model.tableview;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class OrderTableViewSchema {
    private final SimpleStringProperty id;
    private final SimpleStringProperty buyOrSell;
    private final SimpleStringProperty product;
    private final SimpleStringProperty orderType;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty currency;
    private final SimpleLongProperty quantity;

    /**
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param price
     * @param currency
     * @param quantity
     */
    public OrderTableViewSchema(String id,
                                String buyOrSell,
                                String product,
                                String orderType,
                                double price,
                                String currency,
                                long quantity) {
        this.id = new SimpleStringProperty(id);
        this.buyOrSell = new SimpleStringProperty(buyOrSell);
        this.product = new SimpleStringProperty(product);
        this.orderType = new SimpleStringProperty(orderType);
        this.price = new SimpleDoubleProperty(price);
        this.currency = new SimpleStringProperty(currency);
        this.quantity = new SimpleLongProperty(quantity);
    }

    /**
     *
     * @param id
     * @param buyOrSell
     * @param product
     * @param orderType
     * @param price
     * @param currency
     * @param quantity
     */
    public void update (String id,
                        String buyOrSell,
                        String product,
                        String orderType,
                        double price,
                        String currency,
                        long quantity) {
        this.setId(id);
        this.setBuyOrSell(buyOrSell);
        this.setProduct(product);
        this.setOrderType(orderType);
        this.setPrice(price);
        this.setCurrency(currency);
        this.setQuantity(quantity);
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

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
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
}
