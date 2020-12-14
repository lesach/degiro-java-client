package com.github.lesach.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.lesach.client.exceptions.DInvalidCredentialsException;
import com.github.lesach.client.exceptions.DUnauthorizedException;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.client.http.DCommunication;
import com.github.lesach.client.http.DResponse;
import com.github.lesach.client.log.DLog;
import com.github.lesach.client.session.DSession;
import com.github.lesach.client.utils.DCredentials;
import com.github.lesach.client.utils.DUtils;
import com.github.lesach.client.raw.DRawCashFunds;
import com.github.lesach.client.raw.DRawOrders;
import com.github.lesach.client.raw.DRawPortfolio;
import com.github.lesach.client.raw.DRawPortfolioSummary;
import com.github.lesach.client.raw.DRawTransactions;
import com.github.lesach.client.raw.DRawVwdPrice;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 *
 * @author indiketa
 */
public class DeGiroImpl implements DeGiro {

    private final DateTimeFormatter zoneDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");
    private final DCredentials credentials;
    private final DCommunication comm;
    private final DSession session;
    private final Gson gson;
    private final ObjectMapper json = new ObjectMapper() {{
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }};
    private final List<DPriceListener> priceListener = new ArrayList<>();
    private long pollingInterval = TimeUnit.SECONDS.toMillis(5);
    private Timer pricePoller = null;
    private static final String BASE_TRADER_URL = "https://trader.degiro.nl";
    private final Map<String, Long> subscribedVwdIssues;
    private final Type rawPriceData = new TypeToken<List<DRawVwdPrice>>() {
    }.getType();

    private long portfolioSummaryLastUpdate = 0;
    private long portfolioLastUpdate = 0;
    private String currency = "EUR";

    protected DeGiroImpl(DCredentials credentials, DSession session) {
        DLog.debug("Degiro Client implementation creation");
        this.session = session;
        this.credentials = credentials;
        this.comm = new DCommunication(this.session);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(DProductType.class, new DUtils.ProductTypeAdapter());
        builder.registerTypeAdapter(DOrderTime.class, new DUtils.OrderTimeTypeAdapter());
        builder.registerTypeAdapter(DOrderType.class, new DUtils.OrderTypeTypeAdapter());
        builder.registerTypeAdapter(DOrderAction.class, new DUtils.OrderActionTypeAdapter());
        builder.registerTypeAdapter(Date.class, new DUtils.DateTypeAdapter());
        this.gson = builder.create();
        this.subscribedVwdIssues = new HashMap<>(500);
    }

    @Override
    public DPortfolioProducts getPortfolio() throws DeGiroException {

        DPortfolioProducts portfolio;
        ensureLogged();

        try {
            DResponse response = comm.getData(session, "portfolio=" + portfolioLastUpdate, null);
            String data = getResponseData(response);
            DLog.info("getPortfolio: " + data);
            DRawPortfolio rawPortfolio = gson.fromJson(data, DRawPortfolio.class);
            portfolioLastUpdate = rawPortfolio.getPortfolio().getLastUpdated();
            portfolio = DUtils.convert(rawPortfolio, currency);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving portfolio", e);
        }
        return portfolio;
    }

    @Override
    public DPortfolioSummary getPortfolioSummary() throws DeGiroException {

        DPortfolioSummary portfolioSummary;
        ensureLogged();

        try {
            DResponse response = comm.getData(session, "totalPortfolio=" + portfolioSummaryLastUpdate, null);
            String data = getResponseData(response);
            DRawPortfolioSummary rawPortfolioSummary = gson.fromJson(data, DRawPortfolioSummary.class);
            portfolioSummaryLastUpdate = rawPortfolioSummary.getTotalPortfolio().getLastUpdated();
            portfolioSummary = DUtils.convertPortfolioSummary(rawPortfolioSummary.getTotalPortfolio());
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving portfolio", e);
        }
        return portfolioSummary;
    }

    @Override
    public DCashFunds getCashFunds() throws DeGiroException {

        DCashFunds cashFunds;
        ensureLogged();

        try {
            DResponse response = comm.getData(session, "cashFunds=0", null);
            DRawCashFunds rawCashFunds = gson.fromJson(getResponseData(response), DRawCashFunds.class);
            cashFunds = DUtils.convert(rawCashFunds);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving cash funds", e);
        }
        return cashFunds;
    }

    @Override
    public List<DOrder> getOrders() throws DeGiroException {

        DOrders orders;
        ensureLogged();

        try {
            DResponse response = comm.getData(session, "orders=0", null);
            DRawOrders rawOrders = gson.fromJson(getResponseData(response), DRawOrders.class);
            orders = DUtils.convert(rawOrders);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving orders", e);
        }
        return orders.getOrders();
    }

    @Override
    public DLastTransactions getLastTransactions() throws DeGiroException {

        DLastTransactions transactions;
        ensureLogged();

        try {
            DResponse response = comm.getData(session, "transactions=0", null);
            DRawTransactions rawTransactions = gson.fromJson(getResponseData(response), DRawTransactions.class);
            transactions = DUtils.convert(rawTransactions);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving transactions", e);
        }
        return transactions;
    }

    @Override
    public DTransactions getTransactions(Calendar from, Calendar to) throws DeGiroException {

        DTransactions transactions;
        ensureLogged();

        try {
            String fromStr = from.get(Calendar.DATE) + "%2F" + (from.get(Calendar.MONTH) + 1) + "%2F" + from.get(Calendar.YEAR);
            String toStr = to.get(Calendar.DATE) + "%2F" + (to.get(Calendar.MONTH) + 1) + "%2F" + to.get(Calendar.YEAR);

            DResponse response = comm.getUrlData(session.getConfig().getData().getReportingUrl(), "v4/transactions?orderId=&product=&fromDate=" + fromStr + "&toDate=" + toStr + "&groupTransactionsByOrder=false&intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), null);
            transactions = gson.fromJson(getResponseData(response), DTransactions.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving transactions", e);
        }
        return transactions;

    }

    @Override
    public void ensureLogged() throws DeGiroException {
        if (Strings.isNullOrEmpty(session.getJSessionId())) {
            login();
        }
    }

    private void login() throws DeGiroException {

        try {
            DLogin login = new DLogin();
            login.setUsername(credentials.getUsername());
            login.setPassword(credentials.getPassword());

            DResponse response = comm.getUrlData(BASE_TRADER_URL, "/login/secure/login", login);

            if (response.getStatus() != 200) {
                if (response.getStatus() == 400) {
                    throw new DInvalidCredentialsException();
                } else {
                    throw new DeGiroException("Bad login HTTP status " + response.getStatus());
                }
            }

            response = comm.getUrlData(BASE_TRADER_URL, "/login/secure/config", null);
            session.setConfig(gson.fromJson(getResponseData(response), DConfig.class));

            response = comm.getUrlData(session.getConfig().getData().getPaUrl(), "client?sessionId=" + session.getJSessionId(), null);
            session.setClient(gson.fromJson(getResponseData(response), DClient.class));

        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving user information", e);
        }

    }

    private void ensureVwdSession() throws DeGiroException {
        ensureLogged();
        if (session.getVwdSession() == null || session.getLastVwdSessionUsed() == 0 || (System.currentTimeMillis() - session.getLastVwdSessionUsed()) > TimeUnit.SECONDS.toMillis(15)) {
            DLog.info("Renewing VWD session");
            getVwdSession();
            if (!subscribedVwdIssues.isEmpty()) {
                subscribeToPrice(subscribedVwdIssues.keySet());
            }

        }
    }

    private void getVwdSession() throws DeGiroException {

        try {
            List<Header> headers = new ArrayList<>(1);
            headers.add(new BasicHeader("Origin", session.getConfig().getData().getTradingUrl()));
            HashMap<String, String> data = new HashMap<>();
            data.put("referrer", "https://trader.degiro.nl");
            DResponse response = comm.getUrlData("https://degiro.quotecast.vwdservices.com/CORS", "/request_session?version=1.0.20170315&userToken=" + session.getClient().getData().getId(), data, headers);
            HashMap map = gson.fromJson(getResponseData(response), HashMap.class);
            session.setVwdSession((String) map.get("sessionId"));
            session.setLastVwdSessionUsed(System.currentTimeMillis());
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving vwd session", e);
        }
    }

    @Override
    public void setPricePollingInterval(int duration, TimeUnit unit) throws DeGiroException {
        if (pricePoller != null) {
            throw new DeGiroException("Price polling interval must be set before adding price watches");
        }
        pollingInterval = unit.toMillis(duration);
    }

    @Override
    public void addPriceListener(DPriceListener pl) {
        this.priceListener.add(pl);
    }

    @Override
    public void removePriceListener(DPriceListener priceListener) {
        this.priceListener.remove(priceListener);
    }

    @Override
    public synchronized void unsubscribeToPrice(String vwdIssueId) {
        subscribedVwdIssues.remove(vwdIssueId);
    }

    @Override
    public synchronized void subscribeToPrice(String vwdIssueId) throws DeGiroException {
        ArrayList<String> list = new ArrayList<>(1);
        list.add(vwdIssueId);
        subscribeToPrice(list);
    }

    @Override
    public synchronized void subscribeToPrice(Collection<String> vwdIssueId) throws DeGiroException {

        try {

            for (String issueId : vwdIssueId) {
                if (!subscribedVwdIssues.containsKey(issueId)) {
                    subscribedVwdIssues.put(issueId, null);
                }
            }

            requestPriceUpdate();
            DLog.info("Subscribed successfully for issues " + Joiner.on(", ").join(vwdIssueId));

        } catch (IOException e) {
            throw new DeGiroException("IOException while subscribing to issues", e);
        }

        if (pricePoller == null) {
            pricePoller = new Timer("PRICE-POLLER", true);
            pricePoller.scheduleAtFixedRate(new DPriceTimerTask(), 0, pollingInterval);
        }

    }

    private void requestPriceUpdate() throws DeGiroException, IOException {
        ensureVwdSession();
        List<Header> headers = new ArrayList<>(1);
        headers.add(new BasicHeader("Origin", session.getConfig().getData().getTradingUrl()));

        HashMap<String, String> data = new HashMap<>();
        data.put("controlData", generatePriceRequestPayload());

        DResponse response = comm.getUrlData("https://degiro.quotecast.vwdservices.com/CORS", "/" + session.getVwdSession(), data, headers);
        getResponseData(response);
        session.setLastVwdSessionUsed(System.currentTimeMillis());

    }

    private String generatePriceRequestPayload() {

        StringBuilder requestedIssues = new StringBuilder();
        for (String issueId : subscribedVwdIssues.keySet()) {
            Long last = subscribedVwdIssues.get(issueId);
            if (last == null || last > pollingInterval) {
                requestedIssues.append("req(X.BidPrice);req(X.AskPrice);req(X.LastPrice);req(X.LastTime);".replace("X", issueId + ""));
            }
        }
        return requestedIssues.toString();

    }

    private void checkPriceChanges() throws DeGiroException {
        ensureVwdSession();

        try {
            requestPriceUpdate();
            List<Header> headers = new ArrayList<>(1);
            headers.add(new BasicHeader("Origin", session.getConfig().getData().getTradingUrl()));

            DResponse response = comm.getUrlData("https://degiro.quotecast.vwdservices.com/CORS", "/" + session.getVwdSession(), null, headers);
            List<DRawVwdPrice> data = gson.fromJson(getResponseData(response), rawPriceData);

            List<DPrice> prices = DUtils.convert(data);

            for (DPrice price : prices) {
                for(DPriceListener pl : priceListener)
                    pl.priceChanged(price);
            }

        } catch (IOException e) {
            throw new DeGiroException("IOException while subscribing to issues", e);
        }

        if (pricePoller == null) {
            pricePoller = new Timer("Prices", true);
            pricePoller.scheduleAtFixedRate(new DPriceTimerTask(), 0, pollingInterval);
        }
    }

    @Override
    public synchronized void clearPriceSubscriptions() {
        session.setVwdSession(null);
        subscribedVwdIssues.clear();
        pricePoller.cancel();
        pricePoller = null;
    }

    @Override
    public DProductDescriptions getProducts(List<Long> productIds) throws DeGiroException {

        DProductDescriptions products;

        ensureLogged();
        try {
            List<Header> headers = new ArrayList<>(1);
            ArrayList<String> productIdStr = new ArrayList<>(productIds.size());
            for (Long productId : productIds) {
                productIdStr.add(productId + "");
            }
            DResponse response = comm.getUrlData(session.getConfig().getData().getProductSearchUrl(), "v5/products/info?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), productIdStr, headers);
            products = gson.fromJson(getResponseData(response), DProductDescriptions.class);

        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving product information", e);
        }

        return products;
    }

    @Override
    public DProductSearch searchProducts(String text, DProductType type, int limit, int offset) throws DeGiroException {

        if (Strings.isNullOrEmpty(text)) {
            throw new DeGiroException("Nothing to search");
        }

        DProductSearch productSearch;

        ensureLogged();
        try {

            String qs = "&searchText=" + encodeValue(text) + "&requireTotal=true&sortColumns=name&sortTypes=asc";

            if (type != null && type.getTypeCode() != 0) {
                qs += "&productTypeId=" + type.getTypeCode();
            }
            qs += "&limit=" + limit;
            if (offset > 0) {
                qs += "&offset=" + offset;
            }
            // https://trader.degiro.nl/product_search/secure/v5/stocks?searchText=CA&requireTotal=true&sortColumns=name&sortTypes=asc&intAccount=11006752&sessionId=FEB76426B8ACEB3914E57A8B20D647E9.prod_b_113_5
            // https://trader.degiro.nl/product_search/secure/v5/products/lookup?intAccount=0&sessionId=15F3D9AB7B60B24BB07A15C5AD4AA870.prod_b_113_5&searchText=CAC&limit=10 >> HTTP 404
            DResponse response = comm.getUrlData(session.getConfig().getData().getProductSearchUrl(), "v5/products/lookup?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId() + qs, null);
            productSearch = json.readValue(getResponseData(response), DProductSearch.class);

        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving product information", e);
        }

        return productSearch;
    }

    @Override
    public DOrderConfirmation checkOrder(DNewOrder order) throws DeGiroException {

        if (order == null) {
            throw new DeGiroException("Order was null (no order to check)");
        }

        DOrderConfirmation orderConfirmation;
        ensureLogged();
        try {
            DResponse response = comm.getUrlData(session.getConfig().getData().getTradingUrl(), "v5/checkOrder;jsessionid=" + session.getJSessionId() + "?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), orderToMap(order));
            orderConfirmation = gson.fromJson(getResponseData(response), DOrderConfirmation.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while checking order", e);
        }

        return orderConfirmation;
    }

    @Override
    public DPlacedOrder confirmOrder(DNewOrder order, String confirmationId) throws DeGiroException {

        if (order == null) {
            throw new DeGiroException("Order was null (no order to check)");
        }

        if (Strings.isNullOrEmpty(confirmationId)) {
            throw new DeGiroException("ConfirmationId was empty");
        }

        DPlacedOrder placedOrder;

        ensureLogged();
        try {
            DResponse response = comm.getUrlData(session.getConfig().getData().getTradingUrl(), "v5/order/" + confirmationId + ";jsessionid=" + session.getJSessionId() + "?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), orderToMap(order));
            placedOrder = gson.fromJson(getResponseData(response), DPlacedOrder.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while checking order", e);
        }

        return placedOrder;

    }

    @Override
    public DPlacedOrder deleteOrder(String orderId) throws DeGiroException {

        if (Strings.isNullOrEmpty(orderId)) {
            throw new DeGiroException("orderId was empty");
        }

        DPlacedOrder placedOrder;

        ensureLogged();
        try {
            DResponse response = comm.getUrlData(session.getConfig().getData().getTradingUrl(), "v5/order/" + orderId + ";jsessionid=" + session.getJSessionId() + "?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), null, null, "DELETE");
            placedOrder = gson.fromJson(getResponseData(response), DPlacedOrder.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while checking order", e);
        }

        return placedOrder;

    }

    @Override
    public DPlacedOrder updateOrder(DOrder order, BigDecimal limit, BigDecimal stop) throws DeGiroException {

        if (order == null) {
            throw new NullPointerException("Order was null");
        }

        DPlacedOrder placedOrder;

        ensureLogged();
        try {

            Map<String, Object> degiroOrder = new HashMap<>();
            degiroOrder.put("buysell", order.getBuysell().getValue());
            degiroOrder.put("orderType", order.getOrderType().getValue());
            degiroOrder.put("productId", order.getProductId());
            degiroOrder.put("size", order.getSize());
            degiroOrder.put("timeType", order.getOrderType().getValue());
            if (limit != null) {
                degiroOrder.put("price", limit.toPlainString());
            }
            if (stop != null) {
                degiroOrder.put("stopPrice", stop.toPlainString());
            }

            DResponse response = comm.getUrlData(session.getConfig().getData().getTradingUrl(), "v5/order/" + order.getId() + ";jsessionid=" + session.getJSessionId() + "?intAccount=" + session.getClient().getData().getIntAccount() + "&sessionId=" + session.getJSessionId(), degiroOrder, null, "PUT");
            placedOrder = gson.fromJson(getResponseData(response), DPlacedOrder.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while checking order", e);
        }

        return placedOrder;

    }

    @Override
    public DPriceHistory getPriceHistory(Long issueId) throws DeGiroException {

        DPriceHistory priceHistory;

        ensureLogged();
        try {

            DResponse response = comm.getGraph(session, "series=price%3Aissueid%3A" + issueId, "PT1M");
            priceHistory = gson.fromJson(getResponseData(response), DPriceHistory.class);
        } catch (IOException e) {
            throw new DeGiroException("IOException while retrieving price data", e);
        }

        return priceHistory;
    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

    @Override
    public DPriceHistory getPriceHistory(String vwdIdentifierType, String vwdId, LocalDateTime start, LocalDateTime end, String resolution) throws DeGiroException {
        DPriceHistory priceHistory;
        try
        {
            ensureLogged();
            String startOffset = start.atOffset(OffsetDateTime.now().getOffset()).format(zoneDateTimeFormatter).replace(' ', 'T');
            String endOffset = end.plusHours(23).plusMinutes(59).plusSeconds(59).atOffset(OffsetDateTime.now().getOffset()).format(zoneDateTimeFormatter).replace(' ', 'T');;
            // &start=2019-04-25T00:00:00+02:00&end=2019-04-25T23:59:59+02:00
            DResponse response = comm.getGraph(session, "series=" + vwdIdentifierType + "%3A" + encodeValue(vwdId) +
                            "&series=price%3A" + vwdIdentifierType + "%3A" + encodeValue(vwdId) +
                            "&start=" + encodeValue(startOffset) +
                            ((start.compareTo(end) < 0) ? "&end=" + encodeValue(endOffset) : "&period=P1D")
                    , resolution);
            priceHistory = json.readValue(getResponseData(response), DPriceHistory.class);
            // MAJ time according to resolution
            priceHistory.NormalizeResolution();
        }
        catch (IOException | DeGiroException e)
        {
            throw new DeGiroException("IOException while retrieving price data", e);
        }

        return priceHistory;
    }


    public DPriceHistory getPriceHistory(String vwdIdentifierType,
                                         String vwdId,
                                         LocalDateTime start) throws DeGiroException {
        return getPriceHistory(vwdIdentifierType,
                vwdId,
                start,
                start.plusHours(23).plusMinutes(59).plusSeconds(59),
                "PT1M");
    }

    private Map<String, Object> orderToMap(DNewOrder order) {
        Map<String, Object> degiroOrder = new HashMap<>();
        degiroOrder.put("buysell", order.getAction().getValue());
        degiroOrder.put("orderType", order.getOrderType().getValue());
        degiroOrder.put("productId", order.getProductId());
        degiroOrder.put("size", order.getSize());
        degiroOrder.put("timeType", order.getTimeType().getValue());
        if (order.getLimitPrice() != null) {
            degiroOrder.put("price", order.getLimitPrice().toString());
        }
        if (order.getStopPrice() != null) {
            degiroOrder.put("stopPrice", order.getStopPrice().toString());
        }
        return degiroOrder;

    }

    private String getResponseData(DResponse response) throws DeGiroException {

        DLog.info(response.getMethod() + " " + response.getUrl() + " >> HTTP " + response.getStatus());
        String data;

        if (response.getStatus() == 401) {
            DLog.warn("Session expired, clearing session tokens");
            session.clearSession();
            throw new DUnauthorizedException();
        }

        if (response.getStatus() == 200 || response.getStatus() == 201) {
            data = response.getText();
        } else {
            throw new DeGiroException("Unexpected HTTP Status " + response.getStatus() + ": " + response.getMethod() + " " + response.getUrl());
        }

        return data;

    }

    public DSession getSession() {
        return session;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    private class DPriceTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                DeGiroImpl.this.checkPriceChanges();
            } catch (Exception e) {
                DLog.error("Exception while updating prices", e);
            }
        }

    }

    public boolean isConnected() {
        return !Strings.isNullOrEmpty(session.getJSessionId());
    }
}
