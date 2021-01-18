package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.strategy.StrategyCore;
import com.github.lesach.strategy.strategy.TradingStrategy;

import java.io.IOException;

public interface IStockageService {

    TradingStrategy getStrategy();

    void saveStrategy() throws IOException;

    StrategyCore getStrategyCore(String strategyName);
}
