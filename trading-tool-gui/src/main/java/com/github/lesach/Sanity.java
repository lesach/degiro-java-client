package com.github.lesach;

import javafx.beans.property.SimpleBooleanProperty;

public class Sanity {
    private final SimpleBooleanProperty connected;
    private final SimpleBooleanProperty callProductPriceRefresh;
    private final SimpleBooleanProperty positionsPriceRefresh;
    private final SimpleBooleanProperty ordersPriceRefresh;
    private final SimpleBooleanProperty productsPriceRefresh;
    private final SimpleBooleanProperty priceSubscriptionList;

    /**
     * Constructor
     */
    public Sanity() {
        connected = new SimpleBooleanProperty(true);
        callProductPriceRefresh = new SimpleBooleanProperty(true);
        positionsPriceRefresh = new SimpleBooleanProperty(true);
        ordersPriceRefresh = new SimpleBooleanProperty(true);
        productsPriceRefresh = new SimpleBooleanProperty(true);
        priceSubscriptionList = new SimpleBooleanProperty(true);
    }

    public boolean isCallProductPriceRefresh() {
        return callProductPriceRefresh.get();
    }

    public SimpleBooleanProperty callProductPriceRefreshProperty() {
        return callProductPriceRefresh;
    }

    public void setCallProductPriceRefresh(boolean callProductPriceRefresh) {
        this.callProductPriceRefresh.set(callProductPriceRefresh);
    }


    public boolean isPositionsPriceRefresh() {
        return positionsPriceRefresh.get();
    }

    public SimpleBooleanProperty positionsPriceRefreshProperty() {
        return positionsPriceRefresh;
    }

    public void setPositionsPriceRefresh(boolean positionsPriceRefresh) {
        this.positionsPriceRefresh.set(positionsPriceRefresh);
    }


    public boolean isConnected() {
        return connected.get();
    }

    public SimpleBooleanProperty connectedProperty() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected.set(connected);
    }

    public boolean isOrdersPriceRefresh() {
        return ordersPriceRefresh.get();
    }

    public SimpleBooleanProperty ordersPriceRefreshProperty() {
        return ordersPriceRefresh;
    }

    public void setOrdersPriceRefresh(boolean ordersPriceRefresh) {
        this.ordersPriceRefresh.set(ordersPriceRefresh);
    }


    public boolean isPriceSubscriptionList() {
        return priceSubscriptionList.get();
    }

    public SimpleBooleanProperty priceSubscriptionListProperty() {
        return priceSubscriptionList;
    }

    public void setPriceSubscriptionList(boolean priceSubscriptionList) {
        this.priceSubscriptionList.set(priceSubscriptionList);
    }

    public boolean isProductsPriceRefresh() {
        return productsPriceRefresh.get();
    }

    public SimpleBooleanProperty productsPriceRefreshProperty() {
        return productsPriceRefresh;
    }

    public void setProductsPriceRefresh(boolean productsPriceRefresh) {
        this.productsPriceRefresh.set(productsPriceRefresh);
    }
}
