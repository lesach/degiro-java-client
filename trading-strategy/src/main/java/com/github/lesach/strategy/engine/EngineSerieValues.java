
package com.github.lesach.strategy.engine;

import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.serie.SerieBase;
import com.github.lesach.strategy.serie.SerieKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;

public class EngineSerieValues
{
    public SerieBase serieBase;
    public SerieKey Key;
    private final ETimeSerieResolutionType _Resolution;
    public LocalDateTime LastUpdate = LocalDateTime.MIN;
    public LinkedList<EngineSerieValue> Values = new LinkedList<>();

    /// <summary>
    /// Constructor
    /// </summary>
    /// <param name="resolution"></param>
    public EngineSerieValues(SerieKey serieKey, ETimeSerieResolutionType resolution)
    {
        Key = serieKey;
        serieBase = new SerieBase(serieKey, Values);
        _Resolution = resolution;
    }

    /// <summary>
    /// A a value at the end of the serie
    /// </summary>
    /// <param name="dateTime"></param>
    /// <param name="price"></param>
    public void AddValue(LocalDateTime dateTime, BigDecimal price)
    {
        LocalDateTime roundedDateTime = _Resolution.getRoundedDateTime(dateTime);
        EngineSerieValue engineSerieValue = this.Values.stream().filter(v -> v.getDateTime() == roundedDateTime).findFirst().orElse(null);
        if (engineSerieValue == null)
        {
            engineSerieValue = new EngineSerieValue() {{
                setDateTime(roundedDateTime);
                setValue(price);
                AggregationCount = 1;
            }};
            this.Values.add(engineSerieValue);
        }
        else
            engineSerieValue.AverageUpdate(price);
        LastUpdate = LocalDateTime.now();
    }
}
