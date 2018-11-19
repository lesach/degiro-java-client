package io.trading.model;

import cat.indiketa.degiro.model.DPortfolioSummary;
import cat.indiketa.degiro.model.DPriceListener;
import cat.indiketa.degiro.model.DProductDescription;
import io.trading.controller.MainController;
import io.trading.provider.ConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Context {
    private static final Logger logger = LogManager.getLogger(Context.class);

    private String username;
    private String password;
    private ConnectionProvider connection = null;

    public DPortfolioSummary getPortfolioSummary() {
        return portfolioSummary;
    }

    private DPortfolioSummary portfolioSummary;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Getter
     * @return current connection
     */
    public ConnectionProvider getConnection() {
        if (connection == null)
            connection = new ConnectionProvider(username, password);
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
        connection = new ConnectionProvider(username, password);
        portfolioSummary = connection.getPortfolioSummary();
        return portfolioSummary != null;
    }

    /**
     * set listener on price refresh
     * @param listener listener
     */
    public void setPriceListener(DPriceListener listener) {
       connection.setPriceListener(listener);
    }

    /**
     * Clear watch list
     */
    public void clearPriceSubscriptions() {
        connection.clearPriceSubscriptions();;
    }

    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    public void subscribeToPrice(String vwdId) {
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
     * @param text
     */
    public List<DProductDescription> searchProducts(String text) {
        return this.connection.searchProducts(text);
    }

}
