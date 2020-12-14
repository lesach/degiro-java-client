package com.github.lesach.webapp.provider;

import com.github.lesach.client.DPortfolioProduct;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.webapp.model.BasicSchema;
import com.github.lesach.webapp.model.ProductSchema;

import java.util.List;
import java.util.Map;

public interface SubscriptionProviderInterface {

    /**
     *
     * @param newProducts product to add
     * @return true if a ne product is registered
     */
    boolean mergePortfolioProducts(List<DPortfolioProduct> newProducts);

    /**
     *
     * @param list product to add
     * @return true if one product is registered
     */
    boolean mergeSchemas(List<? extends BasicSchema> list);

    /**
     *
     * @param newProducts product to add
     * @return true if a ne product is registered
     */
    boolean mergeDescriptionProducts(List<DProductDescription> newProducts);

    /**
     *
     * @param add product to add
     */
    void registerProduct(BasicSchema add);

    /**
     *
     * @param add product to add
     */
    void registerProduct(DPortfolioProduct add);

    /**
     *
     * @param add product to add
     */
    void registerProduct(DProductDescription add);

    /**
     *
     */
    void manageSubscription();

    /**
     * Accessor
     * @return ProductSchema list
     */
    Map<String, ProductSchema> getProducts();

    /**
     * Accessor
     * @return Subscribed products
     */
    List<String> getSubscribedProducts();
}
