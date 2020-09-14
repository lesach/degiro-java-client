package com.github.lesach;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class InputOrder {
    private static final Logger logger = LogManager.getLogger(Context.class);

    private final SimpleLongProperty productId;
    private final ObjectProperty<BigDecimal> amount;
    private final ObjectProperty<BigDecimal> price;
    private final ObjectProperty<BigDecimal> total;
    private final SimpleLongProperty quantity;

    /**
     *
     */
    public InputOrder() {
        productId = new SimpleLongProperty(0L);
        amount = new SimpleObjectProperty<BigDecimal>(BigDecimal.ZERO);
        price = new SimpleObjectProperty<BigDecimal>(BigDecimal.ZERO);
        total = new SimpleObjectProperty<BigDecimal>(BigDecimal.ZERO);
        quantity = new SimpleLongProperty(0L);

        amount.addListener((observable, oldValue, newValue) -> {
            logger.info("InputOrder.amount changed from " + oldValue + " to " + newValue);
            if (this.price.get().compareTo(BigDecimal.ZERO) != 0)
                this.setQuantity(this.amount.get().divide(this.price.get(), MathContext.DECIMAL64).setScale(0, RoundingMode.FLOOR).longValue());
            else
                this.setQuantity(0L);
        });

        price.addListener((observable, oldValue, newValue) -> {
            logger.info("InputOrder.price changed from " + oldValue + " to " + newValue);
            if (this.price.get().compareTo(BigDecimal.ZERO) != 0)
                this.setQuantity(this.amount.get().divide(this.price.get(), MathContext.DECIMAL64).setScale(0, RoundingMode.FLOOR).longValue());
            else
                this.setQuantity(0L);
        });
    }


    public BigDecimal getAmount() {
        return amount.get();
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount.set(amount);
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
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
        this.setTotal(this.price.get().multiply(BigDecimal.valueOf(this.quantity.get())).setScale(2, RoundingMode.HALF_UP));
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
