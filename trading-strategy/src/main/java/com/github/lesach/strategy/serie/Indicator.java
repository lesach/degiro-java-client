package com.github.lesach.strategy.serie;

import com.github.lesach.strategy.EIndicatorType;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Indicator implements Cloneable {
    public EIndicatorType IndicatorType;
    public List<IndicatorParameter> Parameters = new ArrayList<IndicatorParameter>();

    public Indicator(EIndicatorType indicatorType)
    {
        IndicatorType = indicatorType;
    }

    public Indicator(EIndicatorType indicatorType, Map<String, BigDecimal> parameters)
    {
        IndicatorType = indicatorType;
        for(Map.Entry<String, BigDecimal> p : parameters.entrySet())
            Parameters.add(new IndicatorParameter() {{ Name = p.getKey(); Value = p.getValue(); }});
    }

    @Override
    public Indicator clone() throws CloneNotSupportedException {
        Indicator result = (Indicator) super.clone();
        result.IndicatorType = this.IndicatorType;
        for (IndicatorParameter p : this.Parameters)
            result.Parameters.add(new IndicatorParameter() {{ Name = p.Name; Value = p.Value; }});
        return result;
    }

    @Override
    public String toString() {
        String result = IndicatorType.toString();
        if (this.Parameters.size() == 0)
            return result;
        else
        {
            result += "(";
            result += Strings.join(this.Parameters, ',');
            return result + ")";
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Indicator))
            return false;
        Indicator indicator = (Indicator) obj;
        if (indicator.IndicatorType == IndicatorType)
        {
            return this.Parameters.stream().allMatch(p -> indicator.Parameters.stream().anyMatch(i -> (i.Name.equals(p.Name)) && (i.Value.equals(p.Value))));
        }
        else
            return false;
    }

    @Override
    public int hashCode()
    {
        return IndicatorType.toString().hashCode() + Parameters.stream().mapToInt(p -> p.Value.hashCode()).sum();
    }
}
