package com.github.lesach.strategy.engine;

import com.github.lesach.client.*;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.strategy.DeGiroClientInterface;
import com.github.lesach.strategy.EIndicatorType;
import com.github.lesach.strategy.strategy.*;
import com.github.lesach.client.log.DLog;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.serie.SerieBase;
import com.github.lesach.strategy.serie.SerieKey;
import com.github.lesach.strategy.MeasureModel;
import com.github.lesach.strategy.SerieEventStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StrategyEngine implements StrategyEngineInterface, InitializingBean
{
    @Autowired
    private DeGiroClientInterface deGiroClient;

    public Function<Map.Entry<LocalDateTime, String>, Boolean> PostMessage;
    private ScheduledFuture<?> execute;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    public Subscription subscription;
    public List<EngineProduct> Products = new ArrayList<>();
    public TradingStrategy tradingStrategy = new TradingStrategy();
    public EEngineStatus Status = EEngineStatus.Stopped;
    public LocalDateTime LastComputation = LocalDateTime.MIN;
    private final Computer computer = new Computer();
    public List<EngineMessage> Messages = new ArrayList<>();
    public Runnable EndOfStepAction;
    public List<EngineCommand> EngineCommands = new ArrayList<>();
    public List<DOrder> Orders;
    public List<DPortfolioProduct> PortfolioProducts;

    @Override
    public void afterPropertiesSet() {
        subscription = new Subscription(deGiroClient);
    }

    /// <summary>
    /// Update price of series
    /// </summary>
    /// <param name="price"></param>
    /// <param name="e"></param>
    @Override
    public Boolean UpdateSeries(DPrice price)
    {
        Products.stream().filter(s -> s.Product.getVwdId().equals(price.getIssueId())).findFirst().ifPresent(serie -> serie.UpdatePrice(price));
        return true;
    }

    /// <summary>
    /// Initialize series for the engine
    /// </summary>
    @Override
    public void InitializeSeries(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException, DeGiroException {
        AddMessage("Initialize series");
        List<SerieKey> serieKeys = tradingStrategy.getStrategies()
                .stream()
                .map(StrategyCore::getSteps)
                .flatMap(List::stream)
                .map(s -> s.Groups)
                .flatMap(List::stream)
                .map(s -> s.Conditions)
                .flatMap(List::stream)
                .map(StrategyStepCondition::getParameters)
                .flatMap(List::stream)
                .filter(p -> p.getParameterType() == EStrategyStepConditionParameterType.Product)
                .map(p -> ((SerieBase) p.getValue()).Key)
                .distinct()
                .collect(Collectors.toList());
        for (SerieKey serieKey : serieKeys)
            AddProductIndicator(serieKey.getProduct(), serieKey.getIndicator());
        // Add Product to buy/sell
        for (StrategyCore strategy : tradingStrategy.getStrategies())
        {
            AddProductIndicator(strategy.getProduct(), new Indicator(EIndicatorType.Last));
        }
        for (EngineProduct product : Products)
        {
            product.Initialize(start, end);
        }
        for (StrategyCore strategy : tradingStrategy.getStrategies())
        {
            strategy.setStartDate(FindReference(strategy).stream()
                    .map(MeasureModel::getDateTime).min(LocalDateTime::compareTo)
                    .orElse(null)
            );
        }
        AddMessage("Series initialized");
    }

    /// <summary>
    /// Add product indicator if not exists
    /// </summary>
    /// <param name="newProduct"></param>
    /// <param name="indicator"></param>
    @Override
    public void AddProductIndicator(DProductDescription newProduct, Indicator indicator) throws CloneNotSupportedException {
        EngineProduct product = Products.stream().filter(s -> s.Product.getId() == newProduct.getId()).findFirst().orElse(null);
        if (product == null)
        {
            product = new EngineProduct() {{ Product = newProduct.clone(); }};
            product.Resolution = tradingStrategy.getResolution();
            Products.add(product);
        }
        EngineSerieValues serie = product.Series.stream().filter(s -> s.Key.getIndicator().equals(indicator)).findFirst().orElse(null);
        if (serie == null)
        {
            EngineProduct finalProduct = product;
            serie = new EngineSerieValues(new SerieKey() {{ setProduct(finalProduct.Product); setIndicator(indicator.clone()); }}, tradingStrategy.getResolution()) ;
            product.Series.add(serie);
        }
    }

    /// <summary>
    /// Start engine
    /// </summary>
    @Override
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
                tradingStrategy.getResolution().GetResolutionStepMilliSeconds(),
                TimeUnit.MILLISECONDS);
        Status = EEngineStatus.Started;
        AddMessage("Strategy execution started");
    }

    /// <summary>
    /// Stop Engine
    /// </summary>
    @Override
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
        DLog.info(String.format("Task status is now {%s}", execute.toString()));
        Status = EEngineStatus.Stopped;
        AddMessage("Strategy execution stopped");
    }

    /// <summary>
    /// Execute the strategy
    /// </summary>
    @Override
    public void ExecuteStrategy(LocalDateTime dateTime) throws DeGiroException {
        AddMessage("Start strategy step execution on time " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        Status = EEngineStatus.Computing;
        LocalDateTime date = this.tradingStrategy.getResolution().getRoundedDateTime(dateTime);
        for (EngineProduct product : Products)
            product.RefreshIndicators();
        for (StrategyCore strategy : tradingStrategy.getStrategies())
        {
            LocalDateTime maxDate;
            if (strategy.getStatuses().size() > 0)
                maxDate = this.tradingStrategy.getResolution().getRoundedDateTime(strategy.getStatuses().stream().map(SerieEventStatus::getDate).max(LocalDateTime::compareTo).orElse(null));
            else
                maxDate = this.tradingStrategy.getResolution().getRoundedDateTime(strategy.getStartDate());
            assert maxDate != null;
            List<LocalDateTime> reference = this.tradingStrategy.getResolution().getDateTimeBetween(maxDate, dateTime);
            if (reference.size() > 0)
            {
                computer.ComputeStatus(strategy,
                        reference,
                        Products.stream()
                                .map(p -> p.Series)
                                .flatMap(List::stream)
                                .map(s -> s.serieBase)
                                .collect(Collectors.toList()));
                boolean result = strategy.getStatuses().stream().filter(s ->
                    s.getDate().isAfter(maxDate) || s.getDate().isEqual(maxDate)
                ).map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).orElse(false);

                // Get current state of the portfolio before sending orders
                Orders = deGiroClient.getOrders();
                PortfolioProducts = deGiroClient.getPortfolio().getActive();

                if (result) {
                    // Check if no order are already present and no position
                    if (Orders.stream().noneMatch(o -> o.getProductId() == strategy.getProduct().getId())) {
                        AddMessage("Generate a buy order");
                        EngineCommand engineCommand = new EngineCommand();
                        // Retrieve the last Ask price
                        MeasureModel lastMeasure = FindReference(strategy, EIndicatorType.Ask).stream().max(Comparator.comparing(MeasureModel::getDateTime)).orElse(null);
                        assert lastMeasure != null;
                        engineCommand.NewOrder = new DNewOrder(DOrderAction.BUY,
                            DOrderType.LIMITED,
                            DOrderTime.DAY,
                            strategy.getProduct().getId(),
                            10L,
                            lastMeasure.getValue(),
                            null);

                        EngineCommands.add(engineCommand);
                    }
                    else {
                        AddMessage("An order already exists for the product: " + strategy.getProduct().getId());
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
    @Override
    public void AddMessage(String message) {
        DLog.info(message);
        PostMessage.apply(new AbstractMap.SimpleEntry<>(LocalDateTime.now(), message));
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="computationMargin"></param>
    /// <param name="start"></param>
    /// <param name="end"></param>
    @Override
    public void InitializeSimulation(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException, DeGiroException {
        AddMessage("Initialize Simulation");
        // Clear strategirs
        for (StrategyCore strategy : tradingStrategy.getStrategies())
        {
            strategy.getStatuses().clear();
            strategy.getStatistics().clear();
        }
        InitializeSeries(start, end);
    }

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    @Override
    public List<? extends MeasureModel> FindReference(StrategyCore strategy, EIndicatorType indicatorType)
    {
        AtomicReference<List<? extends MeasureModel>> result = new AtomicReference<>();
        Products.stream().filter(p -> p.Product.equals(strategy.getProduct())).findFirst().flatMap(p -> p.Series
                .stream().filter(s -> s.Key.equals(new SerieKey() {{
                    setProduct(strategy.getProduct());
                    setIndicator(new Indicator(indicatorType));
                }}))
                .findFirst()).ifPresent(e ->
                result.set(e.serieBase.Values));
        return result.get();
    }

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    @Override
    public List<? extends MeasureModel> FindReference(StrategyCore strategy)
    {
        return FindReference(strategy, EIndicatorType.Last);
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="date"></param>
    /// <param name="reference"></param>
    @Override
    public void SimulateStep(LocalDateTime date) throws DeGiroException {
        ExecuteStrategy(date);
        for (StrategyCore strategy : tradingStrategy.getStrategies())
            computer.ComputePeriods(strategy,
                    FindReference(strategy).stream().filter(m -> m.getDateTime().isBefore(date) || m.getDateTime().isEqual(date)).collect(Collectors.toList()));
    }

}
