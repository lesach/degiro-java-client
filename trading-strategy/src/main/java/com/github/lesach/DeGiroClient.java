package com.github.lesach;

import com.github.lesach.exceptions.DeGiroException;
import com.github.lesach.session.DPersistentSession;
import com.github.lesach.utils.DCredentials;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DeGiroClient
{
    @Autowired
    private AppConfig appConfig;

    private static DeGiroClient lazy;

    public static DeGiroClient getInstance() {
        if (lazy == null)
            lazy = new DeGiroClient();
        return lazy;
    }

    private final DeGiro _Degiro;

    public boolean IsConnected() { return this._Degiro.isConnected(); }

    /// <summary>
    /// Provate constructor for singleton
    /// </summary>
    private DeGiroClient()
    {
        DCredentials creds = new DCredentials() {
            @Override
            public String getUsername() {
                return appConfig.username;
            }

            @Override
            public String getPassword() {
                return appConfig.password;
            }
        };
        if (Strings.isNullOrEmpty(appConfig.jsonPeristenSessionPath))
            this._Degiro = DeGiroFactory.newInstance(creds);
        else
            this._Degiro = DeGiroFactory.newInstance(creds, new DPersistentSession(appConfig.jsonPeristenSessionPath));
    }

    /// <summary>
    /// Return Portfolio summary
    /// </summary>
    /// <returns></returns>
    public DPortfolioSummary GetPortfolioSummary() throws DeGiroException {
        return this._Degiro.getPortfolioSummary();
    }

    /// <summary>
    /// Add price listener
    /// </summary>
    /// <param name="priceListener"></param>
    public void AddPriceListener(DPriceListener priceListener)
    {
        this._Degiro.addPriceListener(priceListener);
    }

    /// <summary>
    /// Remove price listener
    /// </summary>
    /// <param name="priceListener"></param>
    public void RemovePriceListener(DPriceListener priceListener)
    {
        this._Degiro.removePriceListener(priceListener);
    }

    /// <summary>
    /// Unsubscribe price
    /// </summary>
    /// <param name="vwdIssueId"></param>
    public void UnsubscribeToPrice(String vwdIssueId)
    {
        this._Degiro.unsubscribeToPrice(vwdIssueId);
    }

    /// <summary>
    /// Subscribe to price listener
    /// </summary>
    /// <param name="vwdIssueId"></param>
    public void SubscribeToPrice(String vwdIssueId) throws DeGiroException {
        this._Degiro.subscribeToPrice(vwdIssueId);
    }

    /// <summary>
    /// Clear all subscription
    /// </summary>
    public void ClearPriceSubscriptions()
    {
        this._Degiro.clearPriceSubscriptions();
    }

    /// <summary>
    /// Return price historical data
    /// </summary>
    /// <param name="vwdIdentifierType"></param>
    /// <param name="vwdId"></param>
    /// <param name="start"></param>
    /// <param name="end"></param>
    /// <param name="resolution"></param>
    /// <returns></returns>
    public DPriceHistory GetPriceHistory(String vwdIdentifierType, String vwdId, LocalDateTime start, LocalDateTime end, String resolution)
    {
        return this._Degiro.getPriceHistory(vwdIdentifierType, vwdId, start, end, resolution);
    }

    /// <summary>
    /// Search a product
    /// </summary>
    /// <param name="text"></param>
    /// <param name="type"></param>
    /// <param name="limit"></param>
    /// <param name="offset"></param>
    /// <returns></returns>
    public DProductSearch SearchProducts(String text, DProductType type, int limit, int offset) throws DeGiroException {
        return this._Degiro.searchProducts(text, type, limit, offset);
    }

    /// <summary>
    /// Return product
    /// </summary>
    public DProductDescriptions getProducts(List<Long> productIds) throws DeGiroException {
        return this._Degiro.getProducts(productIds);
    }

    /// <summary>
    /// Return portfolio
    /// </summary>
    public DPortfolioProducts getPortfolio() throws DeGiroException {
        return this._Degiro.getPortfolio();
    }

    /// <summary>
    /// Return orders
    /// </summary>
    public List<DOrder> getOrders() throws DeGiroException {
        return this._Degiro.getOrders();
    }

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
    public DOrderConfirmation checkOrder(DNewOrder order) throws DeGiroException {
        return this._Degiro.checkOrder(order);
    }

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
    public DPlacedOrder confirmOrder(DNewOrder order, String confirmationId) throws DeGiroException {
        return this._Degiro.confirmOrder(order, confirmationId);
    }

    /**
     * Check if an order is valid
     * @param orderId to delete
     * @return status
     * @throws DeGiroException in case of error
     */
    public DPlacedOrder deleteOrder(String orderId) throws DeGiroException {
        return this._Degiro.deleteOrder(orderId);
    }
}

