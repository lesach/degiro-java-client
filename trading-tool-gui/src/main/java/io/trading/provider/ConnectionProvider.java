package io.trading.provider;

import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.DeGiroFactory;
import cat.indiketa.degiro.model.*;
import cat.indiketa.degiro.utils.DCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ConnectionProvider {
    private static final Logger logger = LogManager.getLogger(ConnectionProvider.class);
    private DeGiro degiro;

    /**
     * Costructor
     * @param username user
     * @param password password
     */
    public ConnectionProvider(String username, String password) {
        DCredentials creds = new DCredentials() {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return password;
            }
        };
        this.degiro = DeGiroFactory.newInstance(creds);
    }


    /**
     * set listener on price refresh
     * @param listener listener
     */
    public void setPriceListener(DPriceListener listener) {
        degiro.setPriceListener(listener);
    }


    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    public void subscribeToPrice(String vwdId) {
        try {
            degiro.subscribeToPrice(vwdId);
        }
        catch (Exception e){
            logger.error("ERROR in subscribeToPrice", e);
        }
    }

    /**
     * Remove product to watch
     * @param vwdId Ids list
     */
    public void unsubscribeToPrice(String vwdId) {
        degiro.unsubscribeToPrice(vwdId);
    }

    /**
     * Clear watch list
     */
    public void clearPriceSubscriptions() {
        degiro.clearPriceSubscriptions();;
    }

    /**
     * Return true if connection is established
     * @return boolean
     */
    public DPortfolioSummary getPortfolioSummary() {
        try {
            return this.degiro.getPortfolioSummary();
        }
        catch (Exception e) {
            logger.error("ERROR in getPortfolioSummary", e);
            return null;
        }
    }

    /**
     * Text to find
     * @param text to find
     * @return list of corresponding products
     */
    public List<DProductDescription> searchProducts(String text) {
        try {
            DProductSearch search = this.degiro.searchProducts(text, DProductType.ALL, 10, 0);
            return search.getProducts();
        }
        catch (Exception e) {
            logger.error("ERROR in searchProducts", e);
            return null;
        }
    }

    /**
     * Return position state
     * @return position list
     */
    public DPortfolioProducts getPortfolio() {
        try {
            return this.degiro.getPortfolio();
        }
        catch (Exception e) {
            logger.error("ERROR in getPortfolio", e);
            return null;
        }
    }


    /**
     * Return orders state
     * @return orders list
     */
    public List<DOrder> getOrders() {
        try {
            return this.degiro.getOrders();
        }
        catch (Exception e) {
            logger.error("ERROR in getOrders", e);
            return null;
        }
    }
}
