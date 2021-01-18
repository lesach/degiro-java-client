
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EBooleanOperator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StrategyStepConditionGroup
{
    private EBooleanOperator booleanOperator;
    private int group;
    private List<StrategyStepCondition> conditions = new ArrayList<>();

    @Override
    public String toString()
    {
        return group + ((this.booleanOperator == EBooleanOperator.NULL) ? "" : " " + this.booleanOperator);
    }

}
