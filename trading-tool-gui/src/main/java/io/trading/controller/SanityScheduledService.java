package io.trading.controller;

import io.trading.config.AppConfig;
import io.trading.model.Context;
import io.trading.model.Sanity;
import io.trading.model.tableview.OrderTableViewSchema;
import io.trading.model.tableview.PositionTableViewSchema;
import io.trading.model.tableview.ProductSchema;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SanityScheduledService extends ScheduledService<Sanity> {
    private static final Logger logger = LogManager.getLogger(SanityScheduledService.class);

    private final int priceRefreshThreshold = AppConfig.getPriceRefreshErrorThreshold();
    private final ObservableList<OrderTableViewSchema> ordersData;
    private final ObservableList<PositionTableViewSchema> positionsData;
    private final Map<String, ProductSchema> products;
    private final List<String> subscribedProducts;
    private final Context context;
    private final ProductSchema callProduct;
    private final ProductSchema putProduct;
    private Sanity sanity;
    /**
     * Constructor
     * @param context Displayed context
     * @param ordersData Displayed orders
     * @param positionsData Displayed positions
     * @param products All used products
     * @param subscribedProducts Subscribed product
     */
    SanityScheduledService(Context context,
                           ObservableList<OrderTableViewSchema> ordersData,
                           ObservableList<PositionTableViewSchema> positionsData,
                           Map<String, ProductSchema> products,
                           List<String> subscribedProducts,
                           ProductSchema callProduct,
                           ProductSchema putProduct,
                           Sanity sanity) {
        this.context = context;
        this.ordersData = ordersData;
        this.positionsData = positionsData;
        this.products = products;
        this.subscribedProducts = subscribedProducts;
        this.callProduct = callProduct;
        this.putProduct = putProduct;
        this.sanity = sanity;
    }

    /**
     * Load positions
     * @return Orders
     */
    protected Task<Sanity> createTask(){
        return new Task<Sanity>(){
            @Override
            protected Sanity call() {
                try {
                    logger.info("Sanity check running...");
                    // Check connection
                    sanity.setConnected(context.isConnected());

                    // Price refresh
                    long now = new Date().getTime();
                    sanity.setPositionsPriceRefresh(positionsData.stream().noneMatch(p -> (now - p.getPriceTime()) > priceRefreshThreshold));
                    sanity.setOrdersPriceRefresh(ordersData.stream().noneMatch(p -> (now - p.getPriceTime()) > priceRefreshThreshold));
                    sanity.setProductsPriceRefresh(products.values().stream().noneMatch(p -> (now - p.getPriceTime()) > priceRefreshThreshold));
                    //sanity.setCallProductPriceRefresh((now - callProduct.getPriceTime()) <= priceRefreshThreshold);

                    // Build expected subscriptions
                    List<String> expected = Stream.concat(
                            Stream.concat(positionsData.stream().map(PositionTableViewSchema::getProductId),
                                    ordersData.stream().map(OrderTableViewSchema::getProductId)),
                            products.values().stream().map(ProductSchema::getVwdId)
                    ).distinct().collect(Collectors.toList());
                    sanity.setPriceSubscriptionList(expected.containsAll(subscribedProducts)
                            && subscribedProducts.containsAll(expected));
                    logger.info("Sanity check end");
                }
                catch (Exception e) {
                    logger.error("ERROR during sanity check", e);
                }
                return sanity;
            }
        };
    }
}
