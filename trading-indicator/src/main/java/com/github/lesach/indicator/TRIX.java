package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/// <summary>
/// Triple Smoothed Exponential Oscillator
/// </summary>
public class TRIX extends IndicatorCalculatorBase<SingleDoubleSerie>
{

    protected int Period = 20;
    protected boolean CalculatePercentage = true;

    public TRIX()
    {

    }

    public TRIX(int period, boolean calculatePercentage)
    {
        this.Period = period;
        this.CalculatePercentage = calculatePercentage;
    }

    /// <summary>
    /// 1 - EMA of Close prices [EMA(Close)]
    /// 2 - BigDecimal smooth [EMA(EMA(Close))]
    /// 3 - Triple smooth [EMA(EMA(EMA(Close)))]
    /// 4 - a) Calculation with percentage: [ROC(EMA(EMA(EMA(Close))))]
    /// 4 - b) Calculation with percentage: [Momentum(EMA(EMA(EMA(Close))))]
    /// </summary>
    /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:trix"/>
    /// <see cref="http://www.fmlabs.com/reference/default.htm?url=TRIX.htm"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        // EMA calculation
        EMA ema = new EMA(Period, false);
        ema.Load(OhlcList);
        List<BigDecimal> emaValues = ema.Calculate().Values;
        for (int i = 0; i < OhlcList.size(); i++)
        {
            OhlcList.get(i).setClose(emaValues.get(i)  != null ? emaValues.get(i) : BigDecimal.ZERO);
        }

        // BigDecimal smooth
        ema.Load(OhlcList.stream().skip(Period - 1).collect(Collectors.toList()));
        List<BigDecimal> doubleSmoothValues = ema.Calculate().Values;
        for (int i = 0; i < Period - 1; i++)
        {
            doubleSmoothValues.add(0, null);
        }
        for (int i = 0; i < OhlcList.size(); i++)
        {
            OhlcList.get(i).setClose(doubleSmoothValues.get(i)  != null ? doubleSmoothValues.get(i) : BigDecimal.ZERO);
        }

        // Triple smooth
        ema.Load(OhlcList.stream().skip(2 * (Period - 1)).collect(Collectors.toList()));
        List<BigDecimal> tripleSmoothValues = ema.Calculate().Values;
        for (int i = 0; i < (2 * (Period - 1)); i++)
        {
            tripleSmoothValues.add(0, null);
        }
        for (int i = 0; i < OhlcList.size(); i++)
        {
            OhlcList.get(i).setClose(tripleSmoothValues.get(i)  != null ? tripleSmoothValues.get(i) : BigDecimal.ZERO);
        }

        // Last step
        SingleDoubleSerie trixSerie;

        if (CalculatePercentage)
        {
            ROC roc = new ROC(1);
            roc.Load(OhlcList.stream().skip(3 * (Period - 1)).collect(Collectors.toList()));
            trixSerie = roc.Calculate();
        }
        else
        {
            Momentum momentum = new Momentum();
            momentum.Load(OhlcList.stream().skip(3 * (Period - 1)).collect(Collectors.toList()));
            trixSerie = momentum.Calculate();
        }

        for (int i = 0; i < (3 * (Period - 1)); i++)
        {
            trixSerie.Values.add(0, null);
        }

        return trixSerie;
    }

}
