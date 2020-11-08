
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EPeriodInstantType;

import java.util.ArrayList;
import java.util.List;

public class StrategyStep
{
    public EPeriodInstantType PeriodInstantType;
    public List<StrategyStepConditionGroup> Groups = new ArrayList<StrategyStepConditionGroup>();
}
