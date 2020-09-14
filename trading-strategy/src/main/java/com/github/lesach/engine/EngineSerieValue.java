
package com.github.lesach.engine;

import com.github.lesach.MeasureModel;

import java.math.BigDecimal;
import java.math.MathContext;

public class EngineSerieValue extends MeasureModel {
    public int AggregationCount;

    public void  AverageUpdate(BigDecimal value)
    {
        setValue((getValue().multiply(BigDecimal.valueOf(AggregationCount)).add(value))
                .divide((BigDecimal.valueOf(AggregationCount + 1)), MathContext.DECIMAL64));
        AggregationCount++;
    }
}
