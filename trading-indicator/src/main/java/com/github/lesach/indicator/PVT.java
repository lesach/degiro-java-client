package com.github.lesach.indicator;

/// <summary>
/// Price Volume Trend (PVT)
/// </summary>
public class PVT extends IndicatorCalculatorBase<SingleDoubleSerie>
{

    /// <summary>
    /// PVT = [((CurrentClose - PreviousClose) / PreviousClose) x Volume] + PreviousPVT
    /// </summary>
    /// <see cref="https://www.tradingview.com/stock-charts-support/index.php/Price_Volume_Trend_(PVT)"/>
    /// <returns></returns>
    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie pvtSerie = new SingleDoubleSerie();
        pvtSerie.Values.add(null);

        for (int i = 1; i < OhlcList.size(); i++)
        {
            pvtSerie.Values.add((((OhlcList.get(i).getClose()
                    .subtract(OhlcList.get(i - 1).getClose()))
                    .divide(OhlcList.get(i - 1).getClose()).multiply(OhlcList.get(i).getVolume())).add(pvtSerie.Values.get(i - 1))));
        }

        return pvtSerie;
    }
}
