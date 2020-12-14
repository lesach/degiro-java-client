package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.strategy.TradingStrategy;

import java.util.List;
import java.util.Map;

public interface IStockageService {

    Map<Integer, TradingStrategy> findStrategies(String name);

    boolean addStrategy(String name);

    TradingStrategy getStrategy(String name);

}
