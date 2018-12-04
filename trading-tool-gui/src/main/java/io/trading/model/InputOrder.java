package io.trading.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputOrder {
    private static final Logger logger = LogManager.getLogger(Context.class);

    private final SimpleLongProperty productId;
    private final SimpleDoubleProperty amount;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty total;
    private final SimpleLongProperty quantity;

    /**
     *
     */
    public InputOrder() {
        productId = new SimpleLongProperty(0L);
        amount = new SimpleDoubleProperty(0d);
        price = new SimpleDoubleProperty(0d);
        total = new SimpleDoubleProperty(0d);
        quantity = new SimpleLongProperty(0L);

        amount.addListener((observable, oldValue, newValue) -> {
            logger.info("InputOrder.amount changed from " + oldValue + " to " + newValue);
            if (this.price.doubleValue() != 0d)
                this.setQuantity(new Double(Math.floor(this.amount.doubleValue() / this.price.doubleValue())).longValue());
            else
                this.setQuantity(0L);
        });

        price.addListener((observable, oldValue, newValue) -> {
            logger.info("InputOrder.price changed from " + oldValue + " to " + newValue);
            if (this.price.doubleValue() != 0d)
                this.setQuantity(Math.round(Math.floor(this.amount.doubleValue() / this.price.doubleValue())));
            else
                this.setQuantity(0L);
        });
    }


    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
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
        this.setTotal(Math.round(this.price.doubleValue() * this.quantity.doubleValue() * 100d) / 100d);
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

    public long getProductId() {
        return productId.get();
    }

    public SimpleLongProperty productIdProperty() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId.set(productId);
    }

}
