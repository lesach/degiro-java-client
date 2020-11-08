package com.github.lesach.strategy.strategy;

import java.math.BigDecimal;

public class ComputationStatistic
{
    public BigDecimal Value;
    public String Name;

    @Override
    public String toString()
    {
        return Name + ": " + Value;
    }
}

