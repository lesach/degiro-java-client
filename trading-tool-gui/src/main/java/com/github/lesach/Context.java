package com.github.lesach;

import com.github.lesach.config.UIConfig;
import com.github.lesach.provider.ConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Context implements TradingContext{
    private static final Logger logger = LogManager.getLogger(Context.class);

    @Autowired
    private UIConfig uiConfig;

    private ConnectionProvider connection = null;

    public DPortfolioSummary getPortfolioSummary() {
        return portfolioSummary;
    }

    private DPortfolioSummary portfolioSummary;

    public String getUsername() {
        return uiConfig.getDegiroUserName();
    }

    public String getPassword() {
        return uiConfig.getDegiroPassword();
    }


    /**
     * Getter
     * @return current connection
     */
    public ConnectionProvider getConnection() {
        if (connection == null)
            connection = new ConnectionProvider();
        return connection;
    }

    /**
     * Constructor
     */
    public Context() {

    }

    /**
     * Connect to Degiro
     * @return true if OK
     */
    public boolean Connect() {
        connection = new ConnectionProvider();
        portfolioSummary = connection.getPortfolioSummary();
        return portfolioSummary != null;
    }

    /**
     * return session state
     * @return session state
     */
    public boolean isConnected() {
        if (connection == null)
            return false;
        else
            return connection.isConnected();

    }

    /**
     * set listener on price refresh
     * @param listener listener
     */
    public void addPriceListener(DPriceListener listener) {
       connection.addPriceListener(listener);
    }

    /**
     * Clear watch list
     */
    public void clearPriceSubscriptions() {
        if (connection != null)
            connection.clearPriceSubscriptions();
    }

    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    public void subscribeToPrice(String vwdId) {
        if (connection != null)
            connection.subscribeToPrice(vwdId);
    }

    /**
     * Remove product to watch
     * @param vwdId Ids list
     */
    public void unsubscribeToPrice(String vwdId) {
        connection.unsubscribeToPrice(vwdId);
    }

    /**
     * Search product
     * @param text to search
     */
    public List<DProductDescription> searchProducts(String text) {
        return this.connection.searchProducts(text);
    }


    /**
     * Return position state
     * @return position list
     */
    public DPortfolioProducts getPortfolio() {
        if (connection != null)
            return this.connection.getPortfolio();
        return null;
    }

    /**
     * Return orders state
     * @return orders list
     */
    public List<DOrder> getOrders() {
        if (connection != null)
            return this.connection.getOrders();
        return null;
    }


    /**
     * Return orders confirmation
     * @return confirmation
     */
    public DOrderConfirmation checkOrder(DNewOrder order ) {
        if (connection != null)
            return this.connection.checkOrder(order);
        return null;
    }

    /**
     * Valid order
     * @return confirmation
     */
    public DPlacedOrder confirmOrder(DNewOrder order, DOrderConfirmation confirmation) {
        if (connection != null)
            return this.connection.confirmOrder(order, confirmation);
        return null;
    }

    /**
     * Remove order
     * @return confirmation
     */
    public DPlacedOrder deleteOrder(String orderId) {
        if (connection != null)
            return this.connection.deleteOrder(orderId);
        return null;
    }
}
