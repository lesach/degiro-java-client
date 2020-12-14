package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/// <summary>
    /// BigDecimal Exponential Moving Average (DEMA)
    /// </summary>
    public class DEMA extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period;

        public DEMA(int period)
        {
            this.Period = period;
        }

        /// <summary>
        /// DEMA = 2 * EMA - EMA of EMA
        /// </summary>
        /// <see cref="http://forex-indicators.net/trend-indicators/dema"/>
        /// <returns></returns>
        @Override
public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie demaSerie = new SingleDoubleSerie();
            EMA ema = new EMA(Period, false);
            ema.Load(OhlcList);
            List<BigDecimal> emaValues = ema.Calculate().Values;

            // assign EMA values to Close price
            for (int i = 0; i < OhlcList.size(); i++)
            {
                OhlcList.get(i).setClose(emaValues.get(i)  != null ? emaValues.get(i) : BigDecimal.ZERO);
            }

            ema.Load(OhlcList.stream().skip(Period - 1).collect(Collectors.toList()));
            // EMA(EMA(value))
            List<BigDecimal> emaEmaValues = ema.Calculate().Values;
            for (int i = 0; i < Period - 1; i++)
            {
                emaEmaValues.add(0, null);
            }    

            // Calculate DEMA
            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= 2 * Period - 2)
                {
                    BigDecimal dema = BigDecimal.valueOf(2).multiply(emaValues.get(i)).subtract(emaEmaValues.get(i));
                    demaSerie.Values.add(dema);
                }
                else
                {
                    demaSerie.Values.add(null);
                }
            }

            return demaSerie;
        }
    }
