package com.github.lesach.indicator;

import java.math.BigDecimal;

/// <summary>
/// On Balance Volume (OBV)
/// </summary>
public class OBV extends IndicatorCalculatorBase<SingleDoubleSerie>
{

    /// <summary>
    /// If today’s close is greater than yesterday’s close then:
    /// OBV(i) = OBV(i-1)+VOLUME(i)
    /// If today’s close is less than yesterday’s close then:
    /// OBV(i) = OBV(i-1)-VOLUME(i)
    /// If today’s close is equal to yesterday’s close then:
    /// OBV(i) = OBV(i-1)
    /// </summary>
    /// <see cref="http://ta.mql4.com/indicators/volumes/on_balance_volume"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie obvSerie = new SingleDoubleSerie();
        obvSerie.Values.add(OhlcList.get(0).Volume);

        for (int i = 1; i < OhlcList.size(); i++)
        {
            BigDecimal value = BigDecimal.ZERO;
            if (OhlcList.get(i).Close.compareTo(OhlcList.get(i - 1).Close) > 0)
            {
                value = obvSerie.Values.get(i - 1).add(OhlcList.get(i).Volume);
            }
            else if (OhlcList.get(i).Close.compareTo(OhlcList.get(i - 1).Close) < 0)
            {
                value = obvSerie.Values.get(i - 1).subtract(OhlcList.get(i).Volume);
            }
            else if (OhlcList.get(i).Close.equals(OhlcList.get(i - 1).Close))
            {
                value = obvSerie.Values.get(i - 1);
            }

            obvSerie.Values.add(value);
        }

        return obvSerie;
    }
}
