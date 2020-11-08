package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Accumulation / Distribution Line
/// </summary>
public class ADL extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    /// <summary>
    /// Acc/Dist = ((Close – Low) – (High – Close)) / (High – Low) * Period's volume
    /// </summary>
    /// <see cref="http://www.investopedia.com/terms/a/accumulationdistribution.asp"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie adlSerie = new SingleDoubleSerie();
        for (Ohlc ohlc : OhlcList)
        {
            BigDecimal value = ((ohlc.Close.subtract(ohlc.Low)).subtract(ohlc.High.subtract(ohlc.Close))
                    .divide(ohlc.High.subtract(ohlc.Low), MathContext.DECIMAL64)
                    .multiply(ohlc.Volume));
            adlSerie.Values.add(value);
        }

        return adlSerie;
    }
}

