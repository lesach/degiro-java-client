package com.github.lesach.strategy.strategy;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComputationStatistic
{
    private BigDecimal value;
    private String name;

    @Override
    public String toString()
    {
        return name + ": " + value;
    }
}

