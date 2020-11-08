
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EBooleanOperator;
import com.github.lesach.strategy.ESerieEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StrategyStepCondition
{
    public EBooleanOperator BooleanOperator;
    public ESerieEventType EventType;
    public List<StrategyStepConditionParameter> Parameters = new ArrayList<StrategyStepConditionParameter>();

    public StrategyStepCondition(ESerieEventType type)
    {
        EventType = type;
    }

    public StrategyStepCondition(ESerieEventType type, List<StrategyStepConditionParameter> parameters)
    {
        EventType = type;
        Parameters.addAll(parameters);
    }

    public <T> T GetParameterValue(String parameterName, Class<T> type)
    {
        StrategyStepConditionParameter parameter = Parameters
                .stream()
                .filter(p -> p.Name.equals(parameterName))
                .findFirst().orElse(null);
        if (parameter != null)
        {
            return type.cast(parameter.getValue());
        }
        return null;
    }

    @Override
    public String toString()
    {
        String result = ((this.BooleanOperator == EBooleanOperator.NULL) ? "" : this.BooleanOperator + "  ")
            + EventType.toString();
        if (this.Parameters.size() == 0)
            return result;
        else
        {
            result += " (";
            result += this.Parameters.stream().map(StrategyStepConditionParameter::toString).collect(Collectors.joining(", "));
            return result + ")";
        }
    }

    public StrategyStepCondition clone()
    {
        StrategyStepCondition result = new StrategyStepCondition(EventType);
        result.BooleanOperator = this.BooleanOperator;
        for (StrategyStepConditionParameter p : Parameters)
            result.Parameters.add(p.clone());
        return result;
    }
}
