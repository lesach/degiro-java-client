package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// William %R
/// </summary>
public class WPR extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected int Period = 14;

    public WPR()
    {

    }

    public WPR(int period)
    {
        this.Period = period;
    }

    /// <summary>
    /// %R = (Highest High - Close)/(Highest High - Lowest Low) * 100
    /// Lowest Low = lowest low for the look-back period
    /// Highest High = highest high for the look-back period
    /// %R is multiplied by -100 correct the inversion and move the decimal.
    /// </summary>
    /// <see cref="http://www.fmlabs.com/reference/default.htm?url=WilliamsR.htm"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie wprSerie = new SingleDoubleSerie();

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (i >= Period - 1)
            {
                BigDecimal highestHigh = HighestHigh(i);
                BigDecimal lowestLow = LowestLow(i);
                BigDecimal wpr = highestHigh.subtract(OhlcList.get(i).Close)
                        .divide(highestHigh.subtract(lowestLow), MathContext.DECIMAL64)
                        .multiply(BigDecimal.valueOf(100L));
                wprSerie.Values.add(wpr);
            }
            else
            {
                wprSerie.Values.add(null);
            }
        }

        return wprSerie;
    }

    private BigDecimal HighestHigh(int index)
    {
        int startIndex = index - (Period - 1);
        int endIndex = index;

        BigDecimal highestHigh = BigDecimal.ZERO;
        for (int i = startIndex; i <= endIndex; i++)
        {
            if (OhlcList.get(i).High.compareTo(highestHigh) > 0)
            {
                highestHigh = OhlcList.get(i).High;
            }
        }

        return highestHigh;
    }

    private BigDecimal LowestLow(int index)
    {
        int startIndex = index - (Period - 1);

        BigDecimal lowestLow = BigDecimal.valueOf(Double.MAX_VALUE);
        for (int i = startIndex; i <= index; i++)
        {
            if (OhlcList.get(i).Low.compareTo(lowestLow) < 0)
            {
                lowestLow = OhlcList.get(i).Low;
            }
        }

        return lowestLow;
    }
}
