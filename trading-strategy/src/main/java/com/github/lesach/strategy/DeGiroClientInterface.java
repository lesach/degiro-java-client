package com.github.lesach.strategy;

import com.github.lesach.client.*;
import com.github.lesach.client.exceptions.DeGiroException;

import java.time.LocalDateTime;
import java.util.List;

public interface DeGiroClientInterface {

    boolean IsConnected();


    /// <summary>
    /// Return Portfolio summary
    /// </summary>
    /// <returns></returns>
    DPortfolioSummary GetPortfolioSummary() throws DeGiroException;

    /// <summary>
    /// Add price listener
    /// </summary>
    /// <param name="priceListener"></param>
    void AddPriceListener(DPriceListener priceListener);

    /// <summary>
    /// Remove price listener
    /// </summary>
    /// <param name="priceListener"></param>
    void RemovePriceListener(DPriceListener priceListener);

    /// <summary>
    /// Unsubscribe price
    /// </summary>
    /// <param name="vwdIssueId"></param>
    void UnsubscribeToPrice(String vwdIssueId);

    /// <summary>
    /// Subscribe to price listener
    /// </summary>
    /// <param name="vwdIssueId"></param>
    void SubscribeToPrice(String vwdIssueId) throws DeGiroException;

    /// <summary>
    /// Clear all subscription
    /// </summary>
    void ClearPriceSubscriptions();

    /// <summary>
    /// Return price historical data
    /// </summary>
    /// <param name="vwdIdentifierType"></param>
    /// <param name="vwdId"></param>
    /// <param name="start"></param>
    /// <param name="end"></param>
    /// <param name="resolution"></param>
    /// <returns></returns>
    DPriceHistory GetPriceHistory(String vwdIdentifierType, String vwdId, LocalDateTime start, LocalDateTime end, String resolution);

    /// <summary>
    /// Search a product
    /// </summary>
    /// <param name="text"></param>
    /// <param name="type"></param>
    /// <param name="limit"></param>
    /// <param name="offset"></param>
    /// <returns></returns>
    DProductSearch SearchProducts(String text, DProductType type, int limit, int offset) throws DeGiroException;

    /// <summary>
    /// Return product
    /// </summary>
    DProductDescriptions getProducts(List<Long> productIds) throws DeGiroException;

    /// <summary>
    /// Return portfolio
    /// </summary>
    DPortfolioProducts getPortfolio() throws DeGiroException;

    /// <summary>
    /// Return orders
    /// </summary>
    List<DOrder> getOrders() throws DeGiroException;

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
     DOrderConfirmation checkOrder(DNewOrder order) throws DeGiroException;

    /**
     * Check if an order is valid
     * @param order to test
     * @return status
     * @throws DeGiroException in case of error
     */
    DPlacedOrder confirmOrder(DNewOrder order, String confirmationId) throws DeGiroException;

    /**
     * Check if an order is valid
     * @param orderId to delete
     * @return status
     * @throws DeGiroException in case of error
     */
    DPlacedOrder deleteOrder(String orderId) throws DeGiroException;
}
