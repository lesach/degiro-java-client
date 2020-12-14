
package com.github.lesach.strategy.strategy;

import com.github.lesach.strategy.EBooleanOperator;
import com.github.lesach.strategy.ESerieEventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class StrategyStepCondition
{
    private EBooleanOperator booleanOperator;
    private ESerieEventType eventType;
    private List<StrategyStepConditionParameter> parameters = new ArrayList<>();

    public StrategyStepCondition(ESerieEventType type)
    {
        eventType = type;
    }

    public StrategyStepCondition(ESerieEventType type, List<StrategyStepConditionParameter> parameters)
    {
        this.eventType = type;
        this.parameters.addAll(parameters);
    }

    public <T> T GetParameterValue(String parameterName, Class<T> type)
    {
        StrategyStepConditionParameter parameter = parameters
                .stream()
                .filter(p -> p.getName().equals(parameterName))
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
        String result = ((this.booleanOperator == EBooleanOperator.NULL) ? "" : this.booleanOperator + "  ")
            + eventType.toString();
        if (this.parameters.size() == 0)
            return result;
        else
        {
            result += " (";
            result += this.parameters.stream().map(StrategyStepConditionParameter::toString).collect(Collectors.joining(", "));
            return result + ")";
        }
    }

    @Override
    public StrategyStepCondition clone() throws CloneNotSupportedException {
        StrategyStepCondition result = (StrategyStepCondition) super.clone();
        result.booleanOperator = this.booleanOperator;
        for (StrategyStepConditionParameter p : parameters)
            result.parameters.add(p.clone());
        return result;
    }



    public static List<StrategyStepCondition> StrategyStepConditions = new ArrayList<StrategyStepCondition>() {{
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.TimeBetween);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Time A");
                setParameterType(EStrategyStepConditionParameterType.Time);
            }});
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Time B");
                setParameterType(EStrategyStepConditionParameterType.Time);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.TimeBefore);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Time");
                setParameterType(EStrategyStepConditionParameterType.Time);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.TimeAfter);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Time");
                setParameterType(EStrategyStepConditionParameterType.Time);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.DerivateSignChangeToIncrease);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.DerivateSignChangeToDecrease);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.CrossDown);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie A");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie B");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
        }});
        //
        add(new StrategyStepCondition() {{
            setEventType(ESerieEventType.CrossUp);
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie A");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
            getParameters().add(new StrategyStepConditionParameter() {{
                setName("Serie B");
                setParameterType(EStrategyStepConditionParameterType.Serie);
            }});
        }});
    }};
}
