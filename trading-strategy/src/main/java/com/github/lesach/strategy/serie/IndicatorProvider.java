package com.github.lesach.strategy.serie;

import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.MeasureModel;

import java.util.ArrayList;
import java.util.List;

public class IndicatorProvider
{

    /// <summary>
    /// Normalize an compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="resolution"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
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
    private List<MeasureModel> Compute(List<MeasureModel> ohlcs, Indicator indicator)
    {
        List<MeasureModel> result = new ArrayList<MeasureModel>();
        switch (indicator.IndicatorType)
        {
            case EMA:
                /*EMA ema = new EMA(Convert.ToInt32(indicator.Parameters.First(p => p.Name == "Period").Value), false);
                ema.Load(ohlcs.Select(o =>  new Ohlc { Date = o.DateTime, Close = o.Value }).ToList());
                SingleDoubleSerie serie = ema.Calculate();
                for (int i = 0; i < ohlcs.size(); i++)
                {
                    if (serie.Values[i].HasValue)
                    {
                        result.add(new MeasureModel()
                        {
                            DateTime = ohlcs[i].DateTime,
                            Value = serie.Values[i].Value
                        });
                    }
                }
                */
                break;
        }
        return result;
    }
}
