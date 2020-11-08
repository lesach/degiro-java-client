package com.github.lesach;

import com.github.lesach.provider.ConnectionProvider;

import java.util.List;

public interface TradingContext {

    String getUsername();

    String getPassword();

    /**
     * Getter
     * @return current connection
     */
    ConnectionProvider getConnection();

    /**
     * Connect to Degiro
     * @return true if OK
     */
    boolean Connect();

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
     * Clear watch list
     */
    void clearPriceSubscriptions();

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
     * Search product
     * @param text to search
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
    DOrderConfirmation checkOrder(DNewOrder order);

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
}
