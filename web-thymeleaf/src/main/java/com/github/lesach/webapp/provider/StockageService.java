package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.strategy.TradingStrategy;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockageService implements IStockageService{

    private final Map<Integer, TradingStrategy> tradingStrategyMap = new HashMap<Integer, TradingStrategy>() {{
        Integer i = 1;
        put(i, new TradingStrategy() {{
            setName("Tototo");
        }});
    }};


    @Override
    public Map<Integer, TradingStrategy> findStrategies(String name) {
        return tradingStrategyMap.entrySet().stream().filter(e ->
                !(Strings.isNullOrEmpty(e.getValue().getName()))
                        && !(Strings.isNullOrEmpty(name))
                        && e.getValue().getName().toLowerCase().contains(name.toLowerCase())
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public boolean addStrategy(String name) {
        Integer i = tradingStrategyMap.keySet().stream().max(Comparator.comparing(e -> e)).orElse(0);
        tradingStrategyMap.put(i + 1, new TradingStrategy() {{
            setName(name);
        }});
        return true;
    }

    @Override
    public TradingStrategy getStrategy(String name) {
        return tradingStrategyMap.values().stream().filter(e ->
                !(Strings.isNullOrEmpty(e.getName()))
                        && !(Strings.isNullOrEmpty(name))
                        && e.getName().toLowerCase().equals(name.toLowerCase())).findFirst().orElse(null);
    }
}
