package io.trading.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class InputOrder {
    private final SimpleDoubleProperty amount;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty total;
    private final SimpleLongProperty quantity;

    /**
     *
     */
    public InputOrder() {
        amount = new SimpleDoubleProperty(0d);
        price = new SimpleDoubleProperty(0d);
        total = new SimpleDoubleProperty(0d);
        quantity = new SimpleLongProperty(0l);
    }


    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
        if (this.price.doubleValue() != 0d)
            this.setQuantity(new Double(Math.floor(this.amount.doubleValue() / this.price.doubleValue())).longValue());
        else
            this.setQuantity(0L);
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

    public long getQuantity() {
        return quantity.get();
    }

    public SimpleLongProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity.set(quantity);
        this.setTotal(this.price.doubleValue() * this.quantity.doubleValue());
    }


    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
        if (this.price.doubleValue() != 0d)
            this.setQuantity(new Double(Math.floor(this.amount.doubleValue() / this.price.doubleValue())).longValue());
        else
            this.setQuantity(0L);
    }
}
