package com.github.lesach.webapp.provider;

import com.github.lesach.client.*;

import java.util.List;
import java.util.Map;

public interface ConnectionServiceInterface {
    /**
     * return session state
     * @return session state
     */
    boolean isConnected();

    /**
     * set listener on price refresh
     * @param listener listener
     */
    void addPriceListener(DPriceListener listener);


    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    void subscribeToPrice(String vwdId);
    /**
     * Remove product to watch
     * @param vwdId Ids list
     */
    void unsubscribeToPrice(String vwdId);

    /**
     * Clear watch list
     */
    void clearPriceSubscriptions();

    /**
     * Return true if connection is established
     * @return boolean
     */
    DPortfolioSummary getPortfolioSummary();
    /**
     * Text to find
     * @param text to find
     * @return list of corresponding products
     */
    List<DProductDescription> searchProducts(String text);

    /**
     * Return position state
     * @return position list
     */
    DPortfolioProducts getPortfolio();

    /**
     * Return orders state
     * @return orders list
     */
    List<DOrder> getOrders();

    /**
     * Return orders confirmation
     * @return confirmation
     */
    DOrderConfirmation checkOrder(DNewOrder order );

    /**
     * Valid order
     * @return confirmation
     */
    DPlacedOrder confirmOrder(DNewOrder order, DOrderConfirmation confirmation);

    /**
     * Remove order
     * @return confirmation
     */
    DPlacedOrder deleteOrder(String orderId);

    /**
     * Return vwdIds
     * @param productIds list
     * @return map
     */
    Map<Long, String> getProductVwdIssueId(List<Long> productIds);
}
