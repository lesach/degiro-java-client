package com.github.lesach.strategy.engine;

import com.github.lesach.client.DPrice;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.strategy.EIndicatorType;
import com.github.lesach.strategy.MeasureModel;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.strategy.StrategyCore;
import com.github.lesach.strategy.strategy.TradingStrategy;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

public interface StrategyEngineInterface
{
    TradingStrategy getStrategy();

    List<EngineProduct> getProducts();

    void setStrategy(TradingStrategy strategy);

    /// <summary>
    /// Update price of series
    /// </summary>
    /// <param name="price"></param>
    /// <param name="e"></param>
    Boolean UpdateSeries(DPrice price);

    /// <summary>
    /// Initialize series for the engine
    /// </summary>
    void InitializeSeries(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException, DeGiroException;

    /// <summary>
    /// Add product indicator if not exists
    /// </summary>
    /// <param name="newProduct"></param>
    /// <param name="indicator"></param>
    void AddProductIndicator(DProductDescription newProduct, Indicator indicator) throws CloneNotSupportedException;

    /// <summary>
    /// Start engine
    /// </summary>
    void Start() throws CloneNotSupportedException, DeGiroException;

    /// <summary>
    /// Stop Engine
    /// </summary>
    void Stop();

    /// <summary>
    /// Execute the strategy
    /// </summary>
    void ExecuteStrategy(LocalDateTime dateTime) throws DeGiroException;

    /// <summary>
    /// Add a message
    /// <summary>
    void AddMessage(String message);

    /// <summary>
    ///
    /// </summary>
    /// <param name="computationMargin"></param>
    /// <param name="start"></param>
    /// <param name="end"></param>
    void InitializeSimulation(LocalDateTime start, LocalDateTime end) throws CloneNotSupportedException, DeGiroException;

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    List<? extends MeasureModel> FindReference(StrategyCore strategy, EIndicatorType indicatorType);

    /// <summary>
    /// Return the strategy reference
    /// </summary>
    /// <param name="strategy"></param>
    /// <returns></returns>
    List<? extends MeasureModel> FindReference(StrategyCore strategy);

    /// <summary>
    ///
    /// </summary>
    /// <param name="date"></param>
    /// <param name="reference"></param>
    void SimulateStep(LocalDateTime date) throws DeGiroException;

    /// <summary>
    ///
    /// </summary>
    /// <param name="start"></param>
    /// <param name="end"></param>
    void Simulate(@NonNull LocalDateTime start, @NonNull LocalDateTime end) throws DeGiroException, CloneNotSupportedException, InterruptedException;
    
}
