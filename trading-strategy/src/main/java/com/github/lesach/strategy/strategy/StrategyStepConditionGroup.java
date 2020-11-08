
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EBooleanOperator;

import java.util.ArrayList;
import java.util.List;

public class StrategyStepConditionGroup
{
    public EBooleanOperator booleanOperator;
    public int Group;
    public List<StrategyStepCondition> Conditions = new ArrayList<StrategyStepCondition>();

    @Override
    public String toString()
    {
        return Integer.toString(Group) + ((this.booleanOperator == EBooleanOperator.NULL) ? "" : " " + this.booleanOperator);
    }

}
