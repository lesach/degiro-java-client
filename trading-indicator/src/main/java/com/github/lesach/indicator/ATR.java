package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/// <summary>
    /// True Range / Average True Range
    /// </summary>
    public class ATR extends IndicatorCalculatorBase<ATRSerie>
    {
        protected int Period = 14;

        public ATR()
        { 
        
        }

        public ATR(int period)
        {
            this.Period = period;
        }

        /// <summary>
        /// TrueHigh = Highest of high[0] or close[-1]
        /// TrueLow = Highest of low[0] or close[-1]
        /// TR = TrueHigh - TrueLow
        /// ATR = EMA(TR)
        /// </summary>
        /// <see cref="http://www.fmlabs.com/reference/default.htm?url=TR.htm"/>
        /// <see cref="http://www.fmlabs.com/reference/default.htm?url=ATR.htm"/>
        /// <returns></returns>
        @Override
        public  ATRSerie Calculate()
        {
            ATRSerie atrSerie = new ATRSerie();
            atrSerie.TrueHigh.add(null);
            atrSerie.TrueLow.add(null);
            atrSerie.TrueRange.add(null);
            atrSerie.ATR.add(null);

            for (int i = 1; i < OhlcList.size(); i++)
            {
                BigDecimal trueHigh = OhlcList.get(i).getHigh().max(OhlcList.get(i - 1).getClose());
                atrSerie.TrueHigh.add(trueHigh);
                BigDecimal trueLow = OhlcList.get(i).getLow().min(OhlcList.get(i - 1).getClose());
                atrSerie.TrueLow.add(trueLow);
                BigDecimal trueRange = trueHigh.subtract(trueLow);
                atrSerie.TrueRange.add(trueRange);    
            }

            for (int i = 1; i < OhlcList.size(); i++)
            {
                OhlcList.get(i).setClose(atrSerie.TrueRange.get(i));
            }

            EMA ema = new EMA(Period, true);
            ema.Load(OhlcList.stream().skip(1).collect(Collectors.toList()));
            List<BigDecimal> atrList = ema.Calculate().Values;
            atrSerie.ATR.addAll(atrList);

            return atrSerie;
        }
    }

