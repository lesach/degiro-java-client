package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Volume Rate of Change (VROC)
/// </summary>
public class VROC extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected int Period;

    public VROC(int period)
    {
        this.Period = period;
    }

    /// <summary>
    /// VROC = ((VOLUME (i) - VOLUME (i - n)) / VOLUME (i - n)) * 100
    /// </summary>
    /// <see cref="http://ta.mql4.com/indicators/volumes/rate_of_change"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie rocSerie = new SingleDoubleSerie();

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (i >= this.Period)
            {
                rocSerie.Values.add(OhlcList.get(i).getVolume()
                        .subtract(OhlcList.get(i - this.Period).getVolume())
                        .divide(OhlcList.get(i - this.Period).getVolume(), MathContext.DECIMAL64)
                        .multiply(BigDecimal.valueOf(100)));
            }
            else
            {
                rocSerie.Values.add(null);
            }
        }

        return rocSerie;
    }
}
