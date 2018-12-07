package io.trading.provider;

import cat.indiketa.degiro.model.DPortfolioProducts;
import cat.indiketa.degiro.model.DProductDescription;
import io.trading.model.Context;
import io.trading.model.tableview.ProductSchema;
import io.trading.model.tableview.BasicSchema;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubscriptionProvider {

    private final Map<String, ProductSchema> products = new HashMap<>();
    private final List<String> subscribedProducts = new ArrayList<>();
    private final Context context;

    /**
     *
     * @param context Connection
     */
    public SubscriptionProvider(Context context) {
        this.context = context;
    }

    /**
     *
     * @param newProducts product to add
     * @return true if a ne product is registered
     */
    public boolean mergePortfolioProducts(List<DPortfolioProducts.DPortfolioProduct> newProducts) {
        boolean oneRegistered = false;
        if (newProducts != null) {
            for (DPortfolioProducts.DPortfolioProduct product : newProducts) {
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
    public boolean mergeDescriptionProducts(ObservableList<? extends BasicSchema> list) {
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
    private void registerProduct(BasicSchema add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(add.getProductId(), pro);
        manageSubscription();
    }

    /**
     *
     * @param add product to add
     */
    private void registerProduct(DPortfolioProducts.DPortfolioProduct add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(add.getId(), pro);
        manageSubscription();
    }

    /**
     *
     * @param add product to add
     */
    private void registerProduct(DProductDescription add) {
        ProductSchema pro = new ProductSchema();
        pro.adopt(add);
        products.put(Long.toString(add.getId()), pro);
        manageSubscription();
    }

    /**
     *
     */
    public void manageSubscription() {
        // Build expected list
        List<String> expected = products.values().stream().map(ProductSchema::getVwdId).collect(Collectors.toList());
        // Add new products
        subscribedProducts.stream().filter(p -> !expected.contains(p) && !p.isEmpty()).forEach(context::unsubscribeToPrice);
        // Remove products
        expected.stream().filter(p -> !subscribedProducts.contains(p) && !p.isEmpty()).forEach(context::subscribeToPrice);
        // replace subscribed
        subscribedProducts.clear();
        subscribedProducts.addAll(expected.stream().filter(p ->  !p.isEmpty()).collect(Collectors.toList()));
    }

    /**
     * Accessor
     * @return ProductSchema list
     */
    public Map<String, ProductSchema> getProducts() {
        return products;
    }

    /**
     * Accessor
     * @return Subscribed products
     */
    public List<String> getSubscribedProducts() {
        return subscribedProducts;
    }
}
