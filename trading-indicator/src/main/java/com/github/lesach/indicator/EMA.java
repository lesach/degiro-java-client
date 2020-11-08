package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
    /// Exponential Moving Average
    /// </summary>
    public class EMA extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period = 10;
        protected boolean Wilder = false;

        public EMA()
        { 
        
        }

        public EMA(int period, boolean wilder)
        {
            this.Period = period;
            this.Wilder = wilder;
        }

        /// <summary>
        /// SMA: 10 period sum / 10 
        /// Multiplier: (2 / (Time periods + 1) ) = (2 / (10 + 1) ) = 0.1818 (18.18%)
        /// EMA: {Close - EMA(previous day)} x multiplier + EMA(previous day). 
        /// for Wilder parameter details: http://www.inside-r.org/packages/cran/TTR/docs/GD
        /// </summary>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_averages"/>
        /// <returns></returns>
        @Override
        public  SingleDoubleSerie Calculate()
        {
            // karşılaştırma için tutarlar ezilebilir. Bağlantı: http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_averages
            //OhlcList[0].Close = 22.27;
            //OhlcList[1].Close = 22.19;
            //OhlcList[2].Close = 22.08;
            //OhlcList[3].Close = 22.17;
            //OhlcList[4].Close = 22.18;
            //OhlcList[5].Close = 22.13;
            //OhlcList[6].Close = 22.23;
            //OhlcList[7].Close = 22.43;
            //OhlcList[8].Close = 22.24;
            //OhlcList[9].Close = 22.29;
            //OhlcList[10].Close = 22.15;
            //OhlcList[11].Close = 22.39;
            //OhlcList[12].Close = 22.38;
            //OhlcList[13].Close = 22.61;
            //OhlcList[14].Close = 23.36;

            SingleDoubleSerie emaSerie = new SingleDoubleSerie();
            BigDecimal multiplier = !this.Wilder ? (BigDecimal.valueOf(2L).divide(BigDecimal.valueOf(Period + 1), MathContext.DECIMAL64)) : BigDecimal.ONE.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);

            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= Period - 1)
                {       
                    BigDecimal close = OhlcList.get(i).Close;
                    BigDecimal emaPrev;
                    if (emaSerie.Values.get(i - 1)  != null)
                    {
                        emaPrev = emaSerie.Values.get(i - 1);
                        BigDecimal ema = close.subtract(emaPrev).multiply(multiplier).add(emaPrev);//(close * multiplier) + (emaPrev * (1 - multiplier));
                        emaSerie.Values.add(ema);
                    }
                    else
                    {
                        BigDecimal sum = BigDecimal.ZERO;
                        for (int j = i; j >= i - (Period - 1); j--)
                        {
                            sum = sum.add(OhlcList.get(j).Close);
                        }
                        BigDecimal ema = sum.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
                        emaSerie.Values.add(ema);
                    }
                }
                else
                {
                    emaSerie.Values.add(null);
                }
            }

            return emaSerie;
        }
    }
