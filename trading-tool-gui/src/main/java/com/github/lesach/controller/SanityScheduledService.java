package com.github.lesach.controller;

import com.github.lesach.TradingContext;
import com.github.lesach.config.UIConfig;
import com.github.lesach.Context;
import com.github.lesach.Sanity;
import com.github.lesach.tableview.OrderTableViewSchema;
import com.github.lesach.tableview.PositionTableViewSchema;
import com.github.lesach.tableview.ProductSchema;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SanityScheduledService extends ScheduledService<Sanity> implements InitializingBean {

    @Autowired
    private UIConfig uiConfig;

    private static final Logger logger = LogManager.getLogger(SanityScheduledService.class);
    private int priceRefreshThreshold;

    private ObservableList<OrderTableViewSchema> ordersData;
    private ObservableList<PositionTableViewSchema> positionsData;
    private Map<String, ProductSchema> products;
    private List<String> subscribedProducts;

    @Autowired
    private TradingContext context;

    private ProductSchema callProduct;
    private ProductSchema putProduct;
    private Sanity sanity;

    @Override
    public void afterPropertiesSet() throws Exception {
        priceRefreshThreshold = uiConfig.getPriceRefreshErrorThreshold();
        MainController tmp = (MainController) mainController;
        this.ordersData = tmp.getOrdersData();
        this.positionsData = tmp.getPositionsData();
        this.products = tmp.getSubscriptionProvider().getProducts();
        this.subscribedProducts = tmp.getSubscriptionProvider().getSubscribedProducts();
        this.callProduct = tmp.getCallProductSchema();
        this.putProduct = tmp.getPutProductSchema();
        this.sanity = tmp.getSanity();
    }

    @Autowired
    @Qualifier("MainController")
    private Initializable mainController;
     /**
     * Constructor
     */
    SanityScheduledService() {

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
