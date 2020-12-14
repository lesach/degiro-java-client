package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Weighted Moving Average
/// </summary>
public class WMA extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected int Period;

    public WMA(int period)
    {
        this.Period = period;
    }

    /// <summary>
    /// Therefore the 5 Day WMA is 83(5/15) + 81(4/15) + 79(3/15) + 79(2/15) + 77(1/15) = 80.7
    /// Day	     1	2	3	4	5 (current)
    /// Price	77	79	79	81	83
    /// WMA	 	 	 	 	    80.7
    /// </summary>
    /// <see cref="http://fxtrade.oanda.com/learn/forex-indicators/weighted-moving-average"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        // karşılaştırma için tutarlar ezilebilir. Bağlantı: http://fxtrade.oanda.com/learn/forex-indicators/weighted-moving-average
        //OhlcList[0].setClose(77;
        //OhlcList[1].setClose(79;
        //OhlcList[2].setClose(79;
        //OhlcList[3].setClose(81;
        //OhlcList[4].setClose(83;

        SingleDoubleSerie wmaSerie = new SingleDoubleSerie();

        int weightSum = 0;
        for (int i = 1; i <= Period; i++)
        {
            weightSum += i;
        }

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (i >= Period - 1)
            {
                BigDecimal wma = BigDecimal.ZERO;
                int weight = 1;
                for (int j = i - (Period - 1); j <= i; j++)
                {
                    wma = wma.add(BigDecimal.valueOf(weight))
                            .divide(BigDecimal.valueOf(weightSum), MathContext.DECIMAL64)
                            .multiply(OhlcList.get(j).getClose());
                    weight++;
                }
                wmaSerie.Values.add(wma);
            }
            else
            {
                wmaSerie.Values.add(null);
            }
        }

        return wmaSerie;
    }
}
