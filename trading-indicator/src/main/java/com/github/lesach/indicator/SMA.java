package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Simple Moving Average
/// </summary>
public class SMA extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected int Period;

    public SMA(int period)
    {
        this.Period = period;
    }

    /// <summary>
    /// Daily Closing Prices: 11,12,13,14,15,16,17
    /// First day of 5-day SMA: (11 + 12 + 13 + 14 + 15) / 5 = 13
    /// Second day of 5-day SMA: (12 + 13 + 14 + 15 + 16) / 5 = 14
    /// Third day of 5-day SMA: (13 + 14 + 15 + 16 + 17) / 5 = 15
    /// </summary>
    /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_averages"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie smaSerie = new SingleDoubleSerie();

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (i >= Period - 1)
            {
                BigDecimal sum = BigDecimal.ZERO;
                for (int j = i; j >= i - (Period - 1); j--)
                {
                    sum = sum.add(OhlcList.get(j).Close);
                }
                BigDecimal avg = sum.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
                smaSerie.Values.add(avg);
            }
            else
            {
                smaSerie.Values.add(null);
            }
        }

        return smaSerie;
    }
}
