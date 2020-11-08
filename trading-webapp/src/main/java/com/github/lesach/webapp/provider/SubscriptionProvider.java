package com.github.lesach.webapp.provider;

import com.github.lesach.client.DPortfolioProduct;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.webapp.model.BasicSchema;
import com.github.lesach.webapp.model.ProductSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriptionProvider implements SubscriptionProviderInterface{
    private final Map<String, ProductSchema> products = new HashMap<>();
    private final List<String> subscribedProducts = new ArrayList<>();

    @Autowired
    private ConnectionServiceInterface connection;

    /**
     *
     * @param newProducts product to add
     * @return true if a ne product is registered
     */
    @Override
    public boolean mergePortfolioProducts(List<DPortfolioProduct> newProducts) {
        boolean oneRegistered = false;
        if (newProducts != null) {
            for (DPortfolioProduct product : newProducts) {
                if (products.containsKey(product.getId())) {
                    products.get(product.getId()).adopt(product);
                } else {
                    registerProduct(product);
                    oneRegistered = true;
                }
            }
        }
        return oneRegistered;
    }


    /**
     *
     * @param list product to add
     * @return true if one product is registered
     */
    @Override
    public boolean mergeSchemas(List<? extends BasicSchema> list) {
        boolean oneRegistered = false;
        if (list != null) {
            for (BasicSchema i : list) {
                if (products.containsKey(i.getProductId())) {
                    products.get(i.getProductId()).adopt(i);
                } else {
                    registerProduct(i);
                    oneRegistered = true;
                }
            }
        }
        return oneRegistered;
    }

    /**
     *
     * @param newProducts product to add
     * @return true if a ne product is registered
     */
    public boolean mergeDescriptionProducts(List<DProductDescription> newProducts) {
        boolean oneRegistered = false;
        if (newProducts != null) {
            for (DProductDescription product : newProducts) {
                if (products.containsKey(Long.toString(product.getId()))) {
                    products.get(Long.toString(product.getId())).adopt(product);
                } else {
                    registerProduct(product);
                    oneRegistered = true;
                }
            }
        }
        return oneRegistered;
    }

    /**
     *
     * @param add product to add
     */
    @Override
    public void registerProduct(BasicSchema add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(add.getProductId(), pro);
        manageSubscription();
    }

    /**
     *
     * @param add product to add
     */
    @Override
    public void registerProduct(DPortfolioProduct add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(add.getId(), pro);
        manageSubscription();
    }

    /**
     *
     * @param add product to add
     */
    @Override
    public void registerProduct(DProductDescription add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(Long.toString(add.getId()), pro);
        manageSubscription();
    }

    /**
     *
     */
    @Override
    public void manageSubscription() {
        // Build expected list
        List<String> expected = products.values().stream().map(ProductSchema::getVwdId).collect(Collectors.toList());
        // Add new products
        subscribedProducts.stream().filter(p -> !expected.contains(p) && !p.isEmpty()).forEach(connection::unsubscribeToPrice);
        // Remove products
        expected.stream().filter(p -> !subscribedProducts.contains(p) && !p.isEmpty()).forEach(connection::subscribeToPrice);
        // replace subscribed
        subscribedProducts.clear();
        subscribedProducts.addAll(expected.stream().filter(p ->  !p.isEmpty()).collect(Collectors.toList()));
    }

    /**
     * Accessor
     * @return ProductSchema list
     */
    @Override
    public Map<String, ProductSchema> getProducts() {
        return products;
    }

    /**
     * Accessor
     * @return Subscribed products
     */
    @Override
    public List<String> getSubscribedProducts() {
        return subscribedProducts;
    }
}
