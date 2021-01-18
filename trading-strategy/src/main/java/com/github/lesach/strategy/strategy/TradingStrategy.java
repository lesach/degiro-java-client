package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.ETimeSerieResolutionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TradingStrategy
{
    private String name;
    private ETimeSerieResolutionType resolution;
    private List<StrategyCore> strategies = new ArrayList<StrategyCore>();

    @Override
    public String toString() {
        return this.getName() + " - " + getResolution().toString();
    }
}
