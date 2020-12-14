package com.github.lesach.strategy;

import com.github.lesach.client.*;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.client.log.DLog;
import com.github.lesach.client.session.DPersistentSession;
import com.github.lesach.client.utils.DCredentials;
import com.google.common.base.Strings;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeGiroClient implements DeGiroClientInterface, InitializingBean
{
    @Autowired
    private AppConfig appConfig;

    private DeGiro _Degiro;

    public boolean IsConnected() { return this._Degiro.isConnected(); }

    @Override
    public void afterPropertiesSet() throws DeGiroException {
        DLog.info("Trying to connect to broker...");
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
        this._Degiro.ensureLogged();
    }

    /// <summary>
    /// Return Portfolio summary
    /// </summary>
    /// <returns></returns>
    @Override
    public DPortfolioSummary GetPortfolioSummary() throws DeGiroException {
        return this._Degiro.getPortfolioSummary();
    }

    /// <summary>
    /// Add price listener
    /// </summary>
    /// <param name="priceListener"></param>
    @Override
    public void AddPriceListener(DPriceListener priceListener)
    {
        this._Degiro.addPriceListener(priceListener);
    }

    /// <summary>
    /// Remove price listener
    /// </summary>
    /// <param name="priceListener"></param>
    @Override
    public void RemovePriceListener(DPriceListener priceListener)
    {
        this._Degiro.removePriceListener(priceListener);
    }

    /// <summary>
    /// Unsubscribe price
    /// </summary>
    /// <param name="vwdIssueId"></param>
    @Override
    public void UnsubscribeToPrice(String vwdIssueId)
    {
        this._Degiro.unsubscribeToPrice(vwdIssueId);
    }

    /// <summary>
    /// Subscribe to price listener
    /// </summary>
    /// <param name="vwdIssueId"></param>
    @Override
    public void SubscribeToPrice(String vwdIssueId) throws DeGiroException {
        this._Degiro.subscribeToPrice(vwdIssueId);
    }

    /// <summary>
    /// Clear all subscription
    /// </summary>
    @Override
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
    @Override
    public DPriceHistory GetPriceHistory(String vwdIdentifierType, String vwdId, LocalDateTime start, LocalDateTime end, String resolution) throws DeGiroException {
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
    @Override
    public DProductSearch SearchProducts(String text, DProductType type, int limit, int offset) throws DeGiroException {
        return this._Degiro.searchProducts(text, type, limit, offset);
    }

    /// <summary>
    /// Return product
    /// </summary>
    @Override
    public DProductDescriptions getProducts(List<Long> productIds) throws DeGiroException {
        return this._Degiro.getProducts(productIds);
    }

    /// <summary>
    /// Return portfolio
    /// </summary>
    @Override
    public DPortfolioProducts getPortfolio() throws DeGiroException {
        return this._Degiro.getPortfolio();
    }

    /// <summary>
    /// Return orders
    /// </summary>
    @Override
    public List<DOrder> getOrders() throws DeGiroException {
        return this._Degiro.getOrders();
    }

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
    @Override
    public DOrderConfirmation checkOrder(DNewOrder order) throws DeGiroException {
        return this._Degiro.checkOrder(order);
    }

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
    @Override
    public DPlacedOrder confirmOrder(DNewOrder order, String confirmationId) throws DeGiroException {
        return this._Degiro.confirmOrder(order, confirmationId);
    }

    /**
     * Check if an order is valid
     * @param orderId to delete
     * @return status
     * @throws DeGiroException in case of error
     */
    @Override
    public DPlacedOrder deleteOrder(String orderId) throws DeGiroException {
        return this._Degiro.deleteOrder(orderId);
    }
}

