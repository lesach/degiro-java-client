package com.github.lesach.tableview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

public class BasicSchema extends ProductSchema {

    private final SimpleLongProperty quantity;
    private final ObjectProperty<BigDecimal> total;
    private final SimpleBooleanProperty error;

    /**
     * Constructor
     */
    public BasicSchema() {
        this.quantity = new SimpleLongProperty(0L);
        this.total =  new SimpleObjectProperty<BigDecimal>(BigDecimal.ZERO);
        this.error = new SimpleBooleanProperty(false);
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

    public BigDecimal getTotal() {
        return total.get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
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
