package com.github.lesach.engine.event;

import com.github.lesach.DPortfolioProduct;

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
