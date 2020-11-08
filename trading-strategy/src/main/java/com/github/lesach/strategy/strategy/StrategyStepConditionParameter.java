
package com.github.lesach.strategy.strategy;

import com.github.lesach.client.DProductDescription;
import com.github.lesach.strategy.serie.SerieKey;

import java.math.BigDecimal;
import java.time.LocalTime;

public class StrategyStepConditionParameter
{
    public String Name;
    public String getDescription() { return GetValueDescription(); }
    public EStrategyStepConditionParameterType ParameterType;
    public DProductDescription Product;

    private Object _value;
    public Object getValue() { return _value; }
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
    public SerieKey Serie;

    @Override
    public String toString()
    {
        return this.Name + "=" + this.getDescription();
    }

    public BigDecimal DoubleValue;

    public LocalTime TimeSpanValue;

    private String GetValueDescription()
    {
        if (ParameterType == EStrategyStepConditionParameterType.Product)
        {
            if (Product != null)
                return Product.getName();
        }
        if (ParameterType == EStrategyStepConditionParameterType.Serie)
        {
            if (Serie != null)
                return Serie.toString();
        }
        if (ParameterType == EStrategyStepConditionParameterType.Number)
        {
            return "Number";
        }
        if (ParameterType == EStrategyStepConditionParameterType.Time)
        {
            return "Time";
        }
        return "Uknown type";
    }

    public StrategyStepConditionParameter clone()
    {
        StrategyStepConditionParameter result = new StrategyStepConditionParameter();
        result.ParameterType = this.ParameterType;
        result.setValue(this.getValue());
        result.Name = this.Name;
        return result;
    }
}
