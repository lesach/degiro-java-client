package io.trading.provider;

import cat.indiketa.degiro.DeGiro;
import cat.indiketa.degiro.DeGiroFactory;
import cat.indiketa.degiro.engine.Product;
import cat.indiketa.degiro.exceptions.DeGiroException;
import cat.indiketa.degiro.log.DLog;
import cat.indiketa.degiro.model.*;
import cat.indiketa.degiro.session.DPersistentSession;
import cat.indiketa.degiro.utils.DCredentials;
import com.google.common.base.Strings;
import io.trading.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        this.degiro = DeGiroFactory.newInstance(creds,  new DPersistentSession(AppConfig.getDegiroPersitentSessionPath()));
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
        degiro.clearPriceSubscriptions();
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


    /**
     * Return orders confirmation
     * @return confirmation
     */
    public DOrderConfirmation checkOrder(DNewOrder order ) {
        try {
            return this.degiro.checkOrder(order);
        }
        catch (Exception e) {
            logger.error("ERROR in checkOrder", e);
            return null;
        }
    }

    /**
     * Valid order
     * @return confirmation
     */
    public DPlacedOrder confirmOrder(DNewOrder order, DOrderConfirmation confirmation) {
        try {
            return this.degiro.confirmOrder(order, confirmation.getConfirmationId());
        }
        catch (Exception e) {
            logger.error("ERROR in confirmOrder", e);
            return null;
        }
    }


    /**
     * Remove order
     * @return confirmation
     */
    public DPlacedOrder deleteOrder(String orderId) {
        try {
            return this.degiro.deleteOrder(orderId);
        }
        catch (Exception e) {
            logger.error("ERROR in deleteOrder", e);
            return null;
        }
    }


    /**
     * Return vwdIds
     * @param productIds
     * @return
     */
    private Map<Long, String> getProductVwdIssueId(List<Long> productIds) {
        HashMap<Long, String> result = new HashMap<>();
        try {
            if (!productIds.isEmpty()) {
                DProductDescriptions descriptions = this.degiro.getProducts(productIds);
                Map<Long, DProductDescription> data = descriptions.getData();
                for (Long productId : data.keySet()) {
                    DProductDescription description = data.get(productId);
                    result.put(productId, description.getVwdId());
                }
            }
        }
        catch (Exception e) {
            logger.error("ERROR in getProductVwdIssueId", e);
        }
        return result;
    }

}
