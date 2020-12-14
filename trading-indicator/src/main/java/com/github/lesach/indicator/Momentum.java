package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;

public class Momentum extends IndicatorCalculatorBase<SingleDoubleSerie>
{

    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie momentumSerie = new SingleDoubleSerie();
        momentumSerie.Values.add(null);

        for (int i = 1; i < OhlcList.size(); i++)
        {
            momentumSerie.Values.add(OhlcList.get(i).getClose().subtract(OhlcList.get(i - 1).getClose()));
        }

        return momentumSerie;
    }

    public SingleDoubleSerie Calculate(List<BigDecimal> values)
    {
        SingleDoubleSerie momentumSerie = new SingleDoubleSerie();
        momentumSerie.Values.add(null);

        for (int i = 1; i < values.size(); i++)
        {
            momentumSerie.Values.add(values.get(i).subtract(values.get(i - 1)));
        }

        return momentumSerie;
    }
}
