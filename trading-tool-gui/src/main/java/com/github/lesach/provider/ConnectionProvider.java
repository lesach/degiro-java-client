package com.github.lesach.provider;

import com.github.lesach.*;
import com.github.lesach.*;
import com.github.lesach.session.DPersistentSession;
import com.github.lesach.utils.DCredentials;
import com.github.lesach.config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionProvider {
    private static final Logger logger = LogManager.getLogger(ConnectionProvider.class);
    private final DeGiroClient degiro = DeGiroClient.getInstance();

    /**
     * Constructor
     */
    public ConnectionProvider() {

    }


    /**
     * return session state
     * @return session state
     */
    public boolean isConnected() {
         return degiro.IsConnected();
    }

    /**
     * set listener on price refresh
     * @param listener listener
     */
    public void addPriceListener(DPriceListener listener) {
        degiro.AddPriceListener(listener);
    }


    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    public void subscribeToPrice(String vwdId) {
        try {
            degiro.SubscribeToPrice(vwdId);
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
        degiro.UnsubscribeToPrice(vwdId);
    }

    /**
     * Clear watch list
     */
    public void clearPriceSubscriptions() {
        degiro.ClearPriceSubscriptions();
    }

    /**
     * Return true if connection is established
     * @return boolean
     */
    public DPortfolioSummary getPortfolioSummary() {
        try {
            return this.degiro.GetPortfolioSummary();
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
            DProductSearch search = this.degiro.SearchProducts(text, DProductType.ALL, 10, 0);
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
     * @param productIds list
     * @return map
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
