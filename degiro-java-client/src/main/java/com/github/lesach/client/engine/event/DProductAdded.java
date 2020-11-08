package com.github.lesach.client.engine.event;

import com.github.lesach.client.DPortfolioProduct;

/**
 *
 * @author indiketa
 */
public class DProductAdded {

    private final DPortfolioProduct product;

    public DProductAdded(DPortfolioProduct product) {
        this.product = product;
    }

    public DPortfolioProduct getProduct() {
        return product;
    }

}
