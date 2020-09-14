
package com.github.lesach.strategy;

import com.github.lesach.EPeriodInstantType;

import java.util.ArrayList;
import java.util.List;

public class StrategyStep
{
    public EPeriodInstantType PeriodInstantType;
    public List<StrategyStepConditionGroup> Groups = new ArrayList<StrategyStepConditionGroup>();
}
