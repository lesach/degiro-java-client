package com.github.lesach.strategy;

import com.github.lesach.ETimeSerieResolutionType;

import java.util.ArrayList;
import java.util.List;

public class TradingStrategy
{
    public ETimeSerieResolutionType Resolution;
    public List<StrategyCore> Strategies = new ArrayList<StrategyCore>();
}
