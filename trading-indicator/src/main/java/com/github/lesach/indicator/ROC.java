package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
/// Rate of Change (ROC)
/// </summary>
public class ROC extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected int Period;

    public ROC(int period)
    {
        this.Period = period;
    }

    /// <summary>
    /// ROC = [(Close - Close n periods ago) / (Close n periods ago)] * 100
    /// </summary>
    /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:rate_of_change_roc_and_momentum"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie rocSerie = new SingleDoubleSerie();

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (i >= this.Period)
            {
                rocSerie.Values.add(OhlcList.get(i).getClose()
                        .subtract(OhlcList.get(i - this.Period).getClose())
                        .divide(OhlcList.get(i - this.Period).getClose(), MathContext.DECIMAL64)
                        .multiply(BigDecimal.valueOf(100L)));
            }
            else
            {
                rocSerie.Values.add(null);
            }
        }

        return rocSerie;
    }
}
