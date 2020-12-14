
package com.github.lesach.strategy.strategy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.strategy.serie.SerieKey;
import com.github.lesach.strategy.service.ValueDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@JsonDeserialize(using = StrategyStepConditionParameterDeserializer.class)
public class StrategyStepConditionParameter
{
    private String Name;

    private EStrategyStepConditionParameterType ParameterType;

    //private DProductDescription Product;
    //public SerieKey Serie;

    private Object value;

    private String getDescription() { return GetValueDescription(); }

    /*
    public void setValue(Object value) {
        if (ParameterType == EStrategyStepConditionParameterType.Product) {
            Product = (DProductDescription) value;
        }
        if (ParameterType == EStrategyStepConditionParameterType.Serie) {
            Serie = (SerieKey) value;
        }
        if (ParameterType == EStrategyStepConditionParameterType.Number) {
            DoubleValue = (BigDecimal) value;
        }
        if (ParameterType == EStrategyStepConditionParameterType.Time) {
            TimeSpanValue = (LocalTime) value;
        }
        _value = value;
    }
    */

    @Override
    public String toString()
    {
        return this.Name + "=" + this.getDescription();
    }

    //public BigDecimal DoubleValue;

    //public LocalTime TimeSpanValue;

    private String GetValueDescription()
    {
        if (value instanceof DProductDescription)
        {
            return ((DProductDescription) value).getName();
        }
        if (value instanceof SerieKey)
        {
            return ((SerieKey) value).toString();
        }
        if (value instanceof BigDecimal)
        {
            return "Number";
        }
        if (value instanceof LocalTime)
        {
            return "Time";
        }
        return "Unknown type";
    }

    public StrategyStepConditionParameter clone() throws CloneNotSupportedException {
        StrategyStepConditionParameter result = (StrategyStepConditionParameter) super.clone();
        result.setParameterType(this.ParameterType);
        result.setValue(this.getValue());
        result.setName(this.Name);
        return result;
    }
}
