package com.github.lesach.strategy.serie;

import com.github.lesach.indicator.EMA;
import com.github.lesach.indicator.Ohlc;
import com.github.lesach.indicator.SingleDoubleSerie;
import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.MeasureModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndicatorProvider implements IndicatorProviderInterface
{

    /// <summary>
    /// Normalize an compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="resolution"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    @Override
    public List<MeasureModel> ComputeIndicator(SerieBase reference,
                                               ETimeSerieResolutionType resolution,
                                               Indicator indicator)
    {
        // Compute indicator only on the "Last" serie
        List<MeasureModel> ohlcs = reference.FormatSerie(resolution);
        return Compute(ohlcs, indicator);
    }


    /// <summary>
    /// Normalize an compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="resolution"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    @Override
    public <T extends MeasureModel> List<MeasureModel> ComputeIndicator(List<T> reference,
        ETimeSerieResolutionType resolution,
        Indicator indicator)
    {
        List<MeasureModel> ohlcs = resolution.Normalize(reference);
        return Compute(ohlcs, indicator);
    }

    /// <summary>
    /// Compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    @Override
    public List<MeasureModel> Compute(List<MeasureModel> ohlcs, Indicator indicator)
    {
        List<MeasureModel> result = new ArrayList<MeasureModel>();
        switch (indicator.getIndicatorType())
        {
            case EMA:
                EMA ema = new EMA(indicator.getParameters().stream().filter(p -> p.getName().equals("Period")).findFirst().get().getValue().intValue(), false);
                ema.Load(ohlcs.stream().map(o ->  new Ohlc() {{ setDate(o.getDateTime()); setClose(o.getValue()); }}).collect(Collectors.toList()));
                SingleDoubleSerie serie = ema.Calculate();
                for (int i = 0; i < ohlcs.size(); i++)
                {
                    if (serie.Values.get(i) != null)
                    {
                        int finalI = i;
                        result.add(new MeasureModel()
                        {{
                            setDateTime(ohlcs.get(finalI).getDateTime());
                            setValue(serie.Values.get(finalI));
                        }});
                    }
                }
                break;
        }
        return result;
    }


    /// <summary>
    /// Compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    @Override
    public List<BigDecimal> ComputeIndicator(List<BigDecimal> ohlcs, Indicator indicator)
    {
        List<BigDecimal> result = new ArrayList<>();
        switch (indicator.getIndicatorType())
        {
            case EMA:
                EMA ema = new EMA(indicator.getParameters().stream().filter(p -> p.getName().equals("Period")).findFirst().get().getValue().intValue(), false);
                ema.Load(ohlcs.stream().map(o ->  new Ohlc() {{ setClose(o); }}).collect(Collectors.toList()));
                SingleDoubleSerie serie = ema.Calculate();
                return serie.Values;
        }
        return result;
    }
}
