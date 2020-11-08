package com.github.lesach.provider;

import com.github.lesach.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConnectionProvider {
    private static final Logger logger = LogManager.getLogger(ConnectionProvider.class);

    @Autowired
    private DeGiroClientInterface deGiroClient;

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
         return deGiroClient.IsConnected();
    }

    /**
     * set listener on price refresh
     * @param listener listener
     */
    public void addPriceListener(DPriceListener listener) {
        deGiroClient.AddPriceListener(listener);
    }


    /**
     * Add product to watch
     * @param vwdId Ids list
     */
    public void subscribeToPrice(String vwdId) {
        try {
            deGiroClient.SubscribeToPrice(vwdId);
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
        deGiroClient.UnsubscribeToPrice(vwdId);
    }

    /**
     * Clear watch list
     */
    public void clearPriceSubscriptions() {
        deGiroClient.ClearPriceSubscriptions();
    }

    /**
     * Return true if connection is established
     * @return boolean
     */
    public DPortfolioSummary getPortfolioSummary() {
        try {
            return this.deGiroClient.GetPortfolioSummary();
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
            DProductSearch search = this.deGiroClient.SearchProducts(text, DProductType.ALL, 10, 0);
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
            return this.deGiroClient.getPortfolio();
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
            return this.deGiroClient.getOrders();
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
            return this.deGiroClient.checkOrder(order);
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
            return this.deGiroClient.confirmOrder(order, confirmation.getConfirmationId());
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
            return this.deGiroClient.deleteOrder(orderId);
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
                DProductDescriptions descriptions = this.deGiroClient.getProducts(productIds);
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
