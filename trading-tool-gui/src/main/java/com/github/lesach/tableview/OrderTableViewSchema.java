package com.github.lesach.tableview;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;

import java.math.BigDecimal;

public class OrderTableViewSchema extends BasicSchema {
    private final SimpleStringProperty id;
    private final SimpleStringProperty buyOrSell;
    private final SimpleStringProperty orderType;
    private final ObjectProperty<BigDecimal> limit;
    private final SimpleStringProperty DUMMY;

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
                                BigDecimal limit,
                                String currency,
                                long quantity) {
        super();
        this.setProductName(product);
        this.setCurrency(currency);
        this.setQuantity(quantity);
        this.id = new SimpleStringProperty(id);
        this.buyOrSell = new SimpleStringProperty(buyOrSell);
        this.orderType = new SimpleStringProperty(orderType);
        this.limit = new SimpleObjectProperty<>(limit);
        this.DUMMY = new SimpleStringProperty(null);
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
                        BigDecimal limit,
                        String currency,
                        long quantity) {
        this.setId(id);
        this.setBuyOrSell(buyOrSell);
        this.setProductName(product);
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
                p.productNameProperty(),
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

    public String getOrderType() {
        return orderType.get();
    }

    public SimpleStringProperty orderTypeProperty() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType.set(orderType);
    }

    public BigDecimal getLimit() {
        return limit.get();
    }

    public ObjectProperty<BigDecimal> limitProperty() {
        return limit;
    }

    public void setLimit(BigDecimal price) {
        this.limit.set(price);
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

    public String getDUMMY() {
        return DUMMY.get();
    }

    public SimpleStringProperty DUMMYProperty() {
        return DUMMY;
    }

    public void setDUMMY(String DUMMY) {
        this.DUMMY.set(DUMMY);
    }

}
