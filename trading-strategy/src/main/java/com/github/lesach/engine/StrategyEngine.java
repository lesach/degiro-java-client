package com.github.lesach.engine;

import com.github.lesach.*;
import com.github.lesach.exceptions.DeGiroException;
import com.github.lesach.log.DLog;
import com.github.lesach.serie.Indicator;
import com.github.lesach.serie.SerieBase;
import com.github.lesach.serie.SerieKey;
import com.github.lesach.strategy.Computer;
import com.github.lesach.strategy.EStrategyStepConditionParameterType;
import com.github.lesach.strategy.StrategyCore;
import com.github.lesach.strategy.TradingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StrategyEngine
{
    public Function<Map.Entry<LocalDateTime, String>, Boolean> PostMessage;
    private ScheduledFuture<?> execute;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public Subscription subscription = new Subscription();
    public List<EngineProduct> Products = new ArrayList<EngineProduct>();
    public TradingStrategy tradingStrategy = new TradingStrategy();
    public EEngineStatus Status = EEngineStatus.Stopped;
    public LocalDateTime LastComputation = LocalDateTime.MIN;
    private final Computer computer = new Computer();
    public List<EngineMessage> Messages = new ArrayList<EngineMessage>();
    public Runnable EndOfStepAction;
    public List<EngineCommand> EngineCommands = new ArrayList<EngineCommand>();
    public List<DOrder> Orders;
    public List<DPortfolioProduct> PortfolioProducts;

    /// <summary>
    /// Constructor
    /// </summary>
    /// <param name="subscription"></param>
    public StrategyEngine()
    {

    }

    /// <summary>
    /// Update price of series
    /// </summary>
    /// <param name="price"></param>
    /// <param name="e"></param>
    private Boolean UpdateSeries(DPrice price)
    {
        Products.stream().filter(s -> s.Product.getVwdId().equals(price.getIssueId())).findFirst().ifPresent(serie -> serie.UpdatePrice(price));
        return true;
    }

    /// <summary>
    /// Initialize series for the engine
    /// </summary>
    private void InitializeSeries(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException {
        AddMessage("Initialize series");
        List<SerieKey> serieKeys = tradingStrategy.Strategies
                .stream()
                .map(s -> s.Steps)
                .flatMap(List::stream)
                .map(s -> s.Groups)
                .flatMap(List::stream)
                .map(s -> s.Conditions)
                .flatMap(List::stream)
                .map(s -> s.Parameters)
                .flatMap(List::stream)
                .filter(p -> p.ParameterType == EStrategyStepConditionParameterType.Product)
                .map(p -> ((SerieBase) p.getValue()).Key)
                .distinct()
                .collect(Collectors.toList());
        for (SerieKey serieKey : serieKeys)
            AddProductIndicator(serieKey.getProduct(), serieKey.getIndicator());
        // Add Product to buy/sell
        for (StrategyCore strategy : tradingStrategy.Strategies)
        {
            AddProductIndicator(strategy.Product, new Indicator(EIndicatorType.Last));
        }
        for (EngineProduct product : Products)
        {
            product.Initialize(start, end);
        }
        for (StrategyCore strategy : tradingStrategy.Strategies)
        {
            strategy.StartDate = FindReference(strategy).stream()
                    .map(MeasureModel::getDateTime).min(LocalDateTime::compareTo)
                    .orElse(null);
        }
        AddMessage("Series initialized");
    }

    /// <summary>
    /// Add product indicator if not exists
    /// </summary>
    /// <param name="newProduct"></param>
    /// <param name="indicator"></param>
    private void AddProductIndicator(DProductDescription newProduct, Indicator indicator) throws CloneNotSupportedException {
        EngineProduct product = Products.stream().filter(s -> s.Product.getId() == newProduct.getId()).findFirst().orElse(null);
        if (product == null)
        {
            product = new EngineProduct() {{ Product = newProduct.clone(); }};
            product.Resolution = tradingStrategy.Resolution;
            Products.add(product);
        }
        EngineSerieValues serie = product.Series.stream().filter(s -> s.Key.getIndicator().equals(indicator)).findFirst().orElse(null);
        if (serie == null)
        {
            EngineProduct finalProduct = product;
            serie = new EngineSerieValues(new SerieKey() {{ setProduct(finalProduct.Product); setIndicator(indicator.clone()); }}, tradingStrategy.Resolution) ;
            product.Series.add(serie);
        }
    }

    /// <summary>
    /// Start engine
    /// </summary>
    public void Start() throws CloneNotSupportedException, DeGiroException {
        AddMessage("Starting strategy execution");
        Status = EEngineStatus.Starting;
        // Initialize Series
        InitializeSeries(LocalDateTime.now().plusHours(-1), LocalDateTime.now());
        // Subscribe necessary product
        for (EngineProduct product : Products)
            subscription.Add(product.Product.getName(), product.Product.getVwdId());
        // Start engine
        subscription.Update = this::UpdateSeries;
        execute = scheduler.scheduleAtFixedRate(() -> {
                    try {
                        ExecuteStrategy(LocalDateTime.now());
                    } catch (DeGiroException e) {
                        DLog.error("Error duringstrategy execution", e);
                    }
                },
                0L,
                tradingStrategy.Resolution.GetResolutionStepMilliSeconds(),
                TimeUnit.MILLISECONDS);
        Status = EEngineStatus.Started;
        AddMessage("Strategy execution started");
    }

    /// <summary>
    /// Stop Engine
    /// </summary>
    public void Stop()
    {
        AddMessage("Stopping strategy execution");
        Status = EEngineStatus.Stopping;
        execute.cancel(true);
        try
        {
            execute.wait();
             // Subscribe necessary product
            for (EngineProduct product : Products)
                subscription.Remove(product.Product.getVwdId());
        }
        catch (InterruptedException e)
        {
            DLog.info("AggregateException thrown with the following inner exceptions:");
            // Display information about each exception.
        }

        // Display status of all tasks.
        DLog.info(String.format("Task status is now {0}", execute.toString()));
        Status = EEngineStatus.Stopped;
        AddMessage("Strategy execution stopped");
    }

    /// <summary>
    /// Execute the strategy
    /// </summary>
    public void ExecuteStrategy(LocalDateTime dateTime) throws DeGiroException {
        AddMessage("Start strategy step execution on time " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        Status = EEngineStatus.Computing;
        LocalDateTime date = this.tradingStrategy.Resolution.getRoundedDateTime(dateTime);
        for (EngineProduct product : Products)
            product.RefreshIndicators();
        for (StrategyCore strategy : tradingStrategy.Strategies)
        {
            LocalDateTime maxDate;
            if (strategy.Statuses.size() > 0)
                maxDate = this.tradingStrategy.Resolution.getRoundedDateTime(strategy.Statuses.stream().map(SerieEventStatus::getDate).max(LocalDateTime::compareTo).orElse(null));
            else
                maxDate = this.tradingStrategy.Resolution.getRoundedDateTime(strategy.StartDate);

            List<LocalDateTime> reference = this.tradingStrategy.Resolution.getDateTimeBetween(maxDate, dateTime);
            if (reference.size() > 0)
            {
                computer.ComputeStatus(strategy,
                        reference,
                        Products.stream()
                                .map(p -> p.Series)
                                .flatMap(List::stream)
                                .map(s -> s.serieBase)
                                .collect(Collectors.toList()));
                boolean result = strategy.Statuses.stream().filter(s -> s.getDate().isAfter(maxDate) || s.getDate().isEqual(maxDate)).map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).get();

                // Get current state of the portfolio before sending orders
                Orders = DeGiroClient.getInstance().getOrders();
                PortfolioProducts = DeGiroClient.getInstance().getPortfolio().getActive();

                if (result) {
                    // Check if no order are already present and no position
                    if (Orders.stream().noneMatch(o -> o.getProductId() == strategy.Product.getId())) {
                        AddMessage("Generate a buy order");
                        EngineCommand engineCommand = new EngineCommand();
                        // Retrieve the last Ask price
                        MeasureModel lastMeasure = FindReference(strategy, EIndicatorType.Ask).stream().max((m1, m2) ->  m1.getDateTime().compareTo(m2.getDateTime())).orElse(null);
                        engineCommand.NewOrder = new DNewOrder(DOrderAction.BUY,
                            DOrderType.LIMITED,
                            DOrderTime.DAY,
                            strategy.Product.getId(),
                            10L,
                            lastMeasure.getValue(),
                            null);

                        EngineCommands.add(engineCommand);
                    }
                    else {
                        AddMessage("An order already exists for the product: " + strategy.Product.getId());
                    }
                }
                else {
                    // Check if no order are already present and no position
                    //if (!PortfolioProducts.Any(o => o.id == strategy.Product.id) {
                    AddMessage("Generate a sell order");
                }
            }
        }
        LastComputation = date;
        if (EndOfStepAction != null)
            EndOfStepAction.run();
        Status = EEngineStatus.Started;
        AddMessage("End strategy step execution");
    }

    /// <summary>
    /// Add a message
    /// <summary>
    private void AddMessage(String message) {
        DLog.info(message);
        PostMessage.apply(new AbstractMap.SimpleEntry<LocalDateTime, String>(LocalDateTime.now(), message));
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="computationMargin"></param>
    /// <param name="start"></param>
    /// <param name="end"></param>
    public void InitializeSimulation(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException {
        AddMessage("Initialize Simulation");
        // Clear strategirs
        for (StrategyCore strategy : tradingStrategy.Strategies)
        {
            strategy.Statuses.clear();
            strategy.Statistics.clear();
        }
        InitializeSeries(start, end);
    }

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    public List<? extends MeasureModel> FindReference(StrategyCore strategy, EIndicatorType indicatorType)
    {
        return Products.stream().filter(p -> p.Product.equals(strategy.Product)).findFirst().orElseThrow()
            .Series
            .stream().filter(s -> s.Key.equals(new SerieKey() {{ setProduct(strategy.Product); setIndicator(new Indicator(indicatorType)); }}))
            .findFirst().orElseThrow()
            .serieBase.Values;
    }

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    public List<? extends MeasureModel> FindReference(StrategyCore strategy)
    {
        return FindReference(strategy, EIndicatorType.Last);
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="date"></param>
    /// <param name="reference"></param>
    public void SimulateStep(LocalDateTime date) throws DeGiroException {
        ExecuteStrategy(date);
        for (StrategyCore strategy : tradingStrategy.Strategies)
            computer.ComputePeriods(strategy,
                    FindReference(strategy).stream().filter(m -> m.getDateTime().isBefore(date) || m.getDateTime().isEqual(date)).collect(Collectors.toList()));
    }
}
