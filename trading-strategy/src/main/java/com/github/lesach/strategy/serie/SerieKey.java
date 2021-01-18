package com.github.lesach.strategy.serie;

import com.github.lesach.client.DProductDescription;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SerieKey implements Cloneable {

    private Indicator indicator;
    private DProductDescription product;

    @Override
    public String toString()
    {
        return product.getName() + " (" + indicator.toString() + ")";
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof SerieKey))
            return false;
        SerieKey serieKey = (SerieKey) obj;
        return (serieKey.getProduct().getId() == product.getId()) && (indicator.equals(serieKey.getIndicator()));
    }

    @Override
    public int hashCode()
    {
        return Long.hashCode(product.getId()) + indicator.hashCode();
    }

    @Override
    public SerieKey clone() throws CloneNotSupportedException {
        SerieKey clone = (SerieKey) super.clone();
        clone.setProduct(product.clone());
        clone.setIndicator(indicator.clone());
        return clone;
    }
}
