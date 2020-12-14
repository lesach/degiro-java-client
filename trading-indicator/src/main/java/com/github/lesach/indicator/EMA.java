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
            //OhlcList[0].setClose(22.27;
            //OhlcList[1].setClose(22.19;
            //OhlcList[2].setClose(22.08;
            //OhlcList[3].setClose(22.17;
            //OhlcList[4].setClose(22.18;
            //OhlcList[5].setClose(22.13;
            //OhlcList[6].setClose(22.23;
            //OhlcList[7].setClose(22.43;
            //OhlcList[8].setClose(22.24;
            //OhlcList[9].setClose(22.29;
            //OhlcList[10].setClose(22.15;
            //OhlcList[11].setClose(22.39;
            //OhlcList[12].setClose(22.38;
            //OhlcList[13].setClose(22.61;
            //OhlcList[14].setClose(23.36;

            SingleDoubleSerie emaSerie = new SingleDoubleSerie();
            BigDecimal multiplier = !this.Wilder ? (BigDecimal.valueOf(2L).divide(BigDecimal.valueOf(Period + 1), MathContext.DECIMAL64)) : BigDecimal.ONE.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);

            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= Period - 1)
                {       
                    BigDecimal close = OhlcList.get(i).getClose();
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
                            sum = sum.add(OhlcList.get(j).getClose());
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
