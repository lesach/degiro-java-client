package com.github.lesach.client.engine;

import com.github.lesach.client.DPortfolioProduct;
import com.github.lesach.client.DeGiro;
import com.github.lesach.client.DeGiroFactory;
import com.github.lesach.client.DPortfolioProducts;
import com.github.lesach.client.DPortfolioSummary;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.client.engine.event.DProductChanged;
import com.github.lesach.client.engine.event.DSummaryChanged;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.client.session.DPersistentSession;
import com.github.lesach.client.log.DLog;
import com.github.lesach.client.DPrice;
import com.github.lesach.client.DPriceListener;
import com.github.lesach.client.DProductDescriptions;
import com.github.lesach.client.utils.DCredentials;
import com.github.lesach.client.utils.DUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.eventbus.AsyncEventBus;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author indiketa
 */
public class DEngine {

    private final PathManager pathManager;
    private final DeGiro degiro;
    private DEngineConfig config;

    private final Map<String, Product> productMap;
    private final Map<String, Product> productMapByIssue;

    private Timer portfolioTimer;
    private Timer portfolioSummaryTimer;
    private final AsyncEventBus eventBus;

    private DPortfolioSummary lastSummary;

    private final static String PORTFOLIO = "PF";
    private final static String SUMMARY = "SM";
    private final static String DESCRIPTION = "DS";
    private final static String PRICES = "PR";
    private final Set<String> inactiveComponents;

    public DEngine(DCredentials credentials) throws IOException {
        this(new DEngineConfig(), credentials);
    }

    public DEngine(DEngineConfig config, DCredentials credentials) throws IOException {
        this.config = config;
        this.pathManager = new PathManager(config.getDataDirectory());
        this.productMap = new HashMap<>();
        this.productMapByIssue = new HashMap<>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        this.eventBus = new AsyncEventBus(executor);
        DLog.info("Creating DeGiro manager instance...");
        File sessionFile = pathManager.getSessionFile(credentials.getUsername());
        degiro = DeGiroFactory.newInstance(credentials, new DPersistentSession(sessionFile));
        degiro.addPriceListener(new DPriceListener() {
            @Override
            public void priceChanged(DPrice price) {
                DEngine.this.inactiveComponents.remove(PRICES);
                DEngine.this.productMapByIssue.get(price.getIssueId()).adopt(price);
            }
        });
        this.inactiveComponents = new HashSet<>();
        this.inactiveComponents.add(PORTFOLIO);
//        this.inactiveComponents.add(SUMMARY);
        this.inactiveComponents.add(DESCRIPTION);
        this.inactiveComponents.add(PRICES);
    }

    public void startEngine() {

        DLog.info("Initializing control plane...");
        startPortfolioTimer();
//        startPortfolioSummaryTimer();

        while (!inactiveComponents.isEmpty()) {
            DLog.info("API client created, waiting for the control plane to become ready. Remaining: [" + Joiner.on(",").join(inactiveComponents) + "]");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {

            }
        }

        DLog.info("Control plane initialized");

    }

    private void startPortfolioTimer() {
        portfolioTimer = new Timer("portfolio", false);
        portfolioTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    DPortfolioProducts portfolio = degiro.getPortfolio();
                    boolean newActiveProducts = mergeProducts(portfolio.getActive());
                    boolean newInactiveProducts = mergeProducts(portfolio.getInactive());

                    inactiveComponents.remove(PORTFOLIO);

                    if (portfolio.getActive().size() + portfolio.getInactive().size() == 0) {
                        inactiveComponents.remove(DESCRIPTION);
                        inactiveComponents.remove(PRICES);
                    }

                    DLog.info("Portfolio refresh completed: " + portfolio.getActive().size() + " active, " + portfolio.getInactive().size() + " inactive.");

                    if (newActiveProducts || newInactiveProducts) {
                        fetchDescriptions();
                    }

                } catch (Exception e) {
                    DLog.error("Exception while refreshing portfolio", e);
                }
            }
        }, config.getPortfolioRefreshInterval() * 1000, config.getPortfolioRefreshInterval() * 1000);

    }

    private boolean mergeProducts(List<DPortfolioProduct> products) throws DeGiroException {

        boolean oneRegistered = false;

        if (products != null) {
            for (DPortfolioProduct product : products) {
                if (productMap.containsKey(product.getId())) {
                    productMap.get(product.getId()).adopt(product);
                } else {
                    registerProduct(product);
                    oneRegistered = true;
                }

            }
        }

        return oneRegistered;
    }

    private void registerProduct(DPortfolioProduct add) throws DeGiroException {
        Product pro = new Product(this);
        pro.adopt(add);
        productMap.put(add.getId(), pro);
        eventBus.post(new DProductChanged(pro));
    }

    private void fetchDescriptions() throws DeGiroException {

        List<String> productIds = new LinkedList<>();
        for (Product product : productMap.values()) {
            if (!product.getId().isEmpty() && product.getId().equals("0")) {
                productIds.add(product.getId());
            }
        }

        if (!productIds.isEmpty()) {
            DLog.info("Fetching " + productIds.size() + " product descriptions");
            DProductDescriptions descriptions = degiro.getProducts(productIds
                    .stream()
                    .filter(DUtils::isNumeric)
                    .map(Long::parseLong)
                    .collect(Collectors.toList()));
            Map<Long, DProductDescription> data = descriptions.getData();
            List<String> vwdIssueId = new LinkedList<>();
            for (Long productId : data.keySet()) {
                DProductDescription description = data.get(productId);
                productMap.get(productId.toString()).adopt(description);
                if (!Strings.isNullOrEmpty(description.getVwdId())) {
                    productMapByIssue.put(description.getVwdId(), productMap.get(productId.toString()));
                    if (productMap.get(productId.toString()).getQty() != 0) {
                        vwdIssueId.add(description.getVwdId());
                    }
                }
            }

            inactiveComponents.remove(DESCRIPTION);
            if (!vwdIssueId.isEmpty()) {
                degiro.subscribeToPrice(vwdIssueId);
            }
        }

    }

    private void startPortfolioSummaryTimer() {
        portfolioSummaryTimer = new Timer("portSumm", false);
        portfolioSummaryTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    DPortfolioSummary summary = degiro.getPortfolioSummary();
                    DLog.info("Portfolio summary refreshed (" + summary.toString() + ").");
                    inactiveComponents.remove(SUMMARY);

                    if (lastSummary != null && lastSummary.hashCode() != summary.hashCode()) {
                        eventBus.post(new DSummaryChanged(summary));
                    }
                    lastSummary = summary;
                } catch (Exception e) {
                    DLog.error("Exception while refreshing portfolio", e);
                }
            }
        }, config.getPortfolioRefreshInterval() * 1000 / 2, config.getPortfolioRefreshInterval() * 1000);

    }

    public DEngineConfig getConfig() {
        return config;
    }

    public DPortfolioSummary getLastSummary() {
        return lastSummary;
    }

    public List<Product> getPortfolio() {
        List<Product> products = new LinkedList<>();
        products.addAll(productMap.values());
        System.out.println(products.size());
        return products;
    }

    AsyncEventBus getEventBus() {
        return eventBus;
    }

    public void register(Object eventReceiver) {
        eventBus.register(eventReceiver);
    }

    public DeGiro getDegiro() {
        return degiro;
    }

}
