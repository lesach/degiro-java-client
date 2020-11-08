package com.github.lesach.strategy.serie;

import com.github.lesach.client.DProductDescription;

public class SerieKey implements Cloneable {
    public Indicator getIndicator() {
        return Indicator;
    }

    public void setIndicator(Indicator indicator) {
        Indicator = indicator;
    }

    public DProductDescription getProduct() {
        return Product;
    }

    public void setProduct(DProductDescription product) {
        Product = product;
    }

    private Indicator Indicator;
    private DProductDescription Product;

    @Override
    public String toString()
    {
        return Product.getName() + " (" + Indicator.toString() + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof SerieKey))
            return false;
        SerieKey serieKey = (SerieKey) obj;
        return (serieKey.Product.getId() == Product.getId()) && (Indicator.equals(serieKey.Indicator));
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(Product.getId()) + Indicator.hashCode();
    }

    @Override
    public SerieKey clone() throws CloneNotSupportedException {
        SerieKey clone = (SerieKey) super.clone();
        clone.setProduct(Product.clone());
        clone.setIndicator(Indicator.clone());
        return clone;
    }
}
