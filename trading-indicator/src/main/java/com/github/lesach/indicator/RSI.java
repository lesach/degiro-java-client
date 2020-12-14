package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Relative Strength Index (RSI)
/// </summary>
public class RSI extends IndicatorCalculatorBase<RSISerie>
{
    protected int Period;

    public RSI(int period)
    {
        this.Period = period;
    }

    /// <summary>
    ///    RS = Average Gain / Average Loss
    ///
    ///                  100
    ///    RSI = 100 - --------
    ///                 1 + RS
    /// </summary>
    /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:relative_strength_index_rsi"/>
    /// <returns></returns>
    @Override
    public  RSISerie Calculate()
    {
        RSISerie rsiSerie = new RSISerie();

        // Add null values for first item, iteration will start from second item of OhlcList
        rsiSerie.RS.add(null);
        rsiSerie.RSI.add(null);

        for (int i = 1; i < OhlcList.size(); i++)
        {
            if (i > this.Period)
            {
                int start = i - Period;
                BigDecimal gainSum = BigDecimal.ZERO;
                for (int j = start; j <= i; j++)
                {
                    BigDecimal thisChange = OhlcList.get(j).getClose().subtract(OhlcList.get(j - 1).getClose());
                    if (thisChange.compareTo(BigDecimal.ZERO) > 0)
                    {
                        gainSum = gainSum.add(thisChange);
                    }
                }
                BigDecimal averageGain = gainSum.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
                BigDecimal lossSum = BigDecimal.ZERO;
                for (int j = start; j <= i; j++)
                {
                    BigDecimal thisChange = OhlcList.get(j).getClose().subtract(OhlcList.get(j - 1).getClose());
                    if (thisChange.compareTo(BigDecimal.ZERO) < 0)
                    {
                        lossSum = lossSum.add(thisChange);
                    }
                }
                BigDecimal averageLoss = BigDecimal.valueOf(-1L).multiply(lossSum).divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
                BigDecimal rs = averageGain.divide(averageLoss, MathContext.DECIMAL64);
                rsiSerie.RS.add(rs);
                BigDecimal rsi = BigDecimal.valueOf(100L)
                        .subtract(BigDecimal.valueOf(100L)
                                .divide(BigDecimal.ONE.add(rs), MathContext.DECIMAL64));
                rsiSerie.RSI.add(rsi);
            }
            else
            {
                rsiSerie.RS.add(null);
                rsiSerie.RSI.add(null);
            }
        }
        return rsiSerie;
    }
}
