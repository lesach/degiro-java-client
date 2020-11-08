package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/// <summary>
    /// Commodity Channel Index (CCI)
    /// </summary>
    public class CCI extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period = 20;
        protected BigDecimal Factor = BigDecimal.valueOf(0.015D);

        public CCI()
        { 
        
        }

        public CCI(int period, BigDecimal factor)
        {
            this.Period = period;
            this.Factor = factor;
        }

        /// <summary>
        /// Commodity Channel Index (CCI)
        /// tp = (high + low + close) / 3
        /// cci = (tp - SMA(tp)) / (Factor * meanDeviation(tp))
        /// </summary>
        /// <see cref="http://www.fmlabs.com/reference/default.htm?url=CCI.htm"/>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:commodity_channel_index_cci"/>
        /// <returns></returns>
        @Override
        public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie cciSerie = new SingleDoubleSerie();

            for (Ohlc ohlc : OhlcList) {
                ohlc.Close = (ohlc.High.add(ohlc.Low).add(ohlc.Close)).divide(BigDecimal.valueOf(3L), MathContext.DECIMAL64);
            }

            SMA sma = new SMA(Period);
            sma.Load(OhlcList);
            List<BigDecimal> smaList = new ArrayList<>(sma.Calculate().Values);

            List<BigDecimal> meanDeviationList = new ArrayList<BigDecimal>();
            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= Period - 1)
                {
                    BigDecimal total = BigDecimal.ZERO;
                    for (int j = i; j >= i - (Period - 1); j--)
                    {
                        total = total.add(smaList.get(i).subtract(OhlcList.get(j).Close).abs());
                    }
                    meanDeviationList.add(total.divide(new BigDecimal(Period), MathContext.DECIMAL64));
                    
                    BigDecimal cci = (OhlcList.get(i).Close.subtract(smaList.get(i)))
                            .divide(Factor.multiply(meanDeviationList.get(i)), MathContext.DECIMAL64);
                    cciSerie.Values.add(cci);
                }
                else
                {
                    meanDeviationList.add(null);
                    cciSerie.Values.add(null);
                }
            }

            return cciSerie;
        }
    }
