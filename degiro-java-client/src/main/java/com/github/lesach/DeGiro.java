package com.github.lesach;

import com.github.lesach.exceptions.DeGiroException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author indiketa
 */
public interface DeGiro {

    DCashFunds getCashFunds() throws DeGiroException;

    DLastTransactions getLastTransactions() throws DeGiroException;

    List<DOrder> getOrders() throws DeGiroException;

    DPortfolioProducts getPortfolio() throws DeGiroException;

    DPortfolioSummary getPortfolioSummary() throws DeGiroException;

    DTransactions getTransactions(Calendar from, Calendar to) throws DeGiroException;

    void addPriceListener(DPriceListener priceListener);

    void removePriceListener(DPriceListener priceListener);

    void setPricePollingInterval(int duration, TimeUnit unit) throws DeGiroException;

    void unsubscribeToPrice(String vwdIssueId) ;
    
    void subscribeToPrice(String vwdIssueId) throws DeGiroException;

    void subscribeToPrice(Collection<String> vwdIssueId) throws DeGiroException;

    void clearPriceSubscriptions();

    DProductSearch searchProducts(String text, DProductType type, int limit, int offset) throws DeGiroException;

    DProductDescriptions getProducts(List<Long> productIds) throws DeGiroException;

    DOrderConfirmation checkOrder(DNewOrder order) throws DeGiroException;

    DPlacedOrder confirmOrder(DNewOrder order, String confirmationId) throws DeGiroException;

    DPlacedOrder deleteOrder(String orderId) throws DeGiroException;

    DPlacedOrder updateOrder(DOrder order, BigDecimal limit, BigDecimal stop) throws DeGiroException;

    DPriceHistory getPriceHistory(Long issueId) throws DeGiroException;

    DPriceHistory getPriceHistory(String vwdIdentifierType, String vwdId, LocalDateTime start, LocalDateTime end, String resolution);

    boolean isConnected();

}
