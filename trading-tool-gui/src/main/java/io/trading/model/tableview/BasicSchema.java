package io.trading.model.tableview;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class BasicSchema extends ProductSchema {

    private final SimpleLongProperty quantity;
    private final SimpleDoubleProperty total;
    private final SimpleBooleanProperty error;

    /**
     * Constructor
     */
    public BasicSchema() {
        this.quantity = new SimpleLongProperty(0L);
        this.total = new SimpleDoubleProperty(0d);
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

    public double getTotal() {
        return total.get();
    }

    public SimpleDoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
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
