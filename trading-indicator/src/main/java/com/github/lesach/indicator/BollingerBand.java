package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
    /// Bollinger Bands
    /// </summary>
    public class BollingerBand extends IndicatorCalculatorBase<BollingerBandSerie>
    {
        protected int Period = 20;
        protected int Factor = 2;

        public BollingerBand()
        { 
        
        }

        public BollingerBand(int period, int factor)
        {
            this.Period = period;
            this.Factor = factor;
        }

        /// <summary>
        /// tp = (high + low + close) / 3
        /// MidBand = SMA(TP)
        /// UpperBand = MidBand + Factor * Stdev(tp)
        /// LowerBand = MidBand - Factor * Stdev(tp)
        /// BandWidth = (UpperBand - LowerBand) / MidBand
        /// </summary>
        /// <see cref="http://www.fmlabs.com/reference/default.htm?url=Bollinger.htm"/>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_bands"/>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:bollinger_band_width"/>
        /// <returns></returns>
        @Override
        public  BollingerBandSerie Calculate()
        {
            BollingerBandSerie bollingerBandSerie = new BollingerBandSerie();   

            BigDecimal totalAverage = BigDecimal.ZERO;
            BigDecimal totalSquares = BigDecimal.ZERO;

            for (int i = 0; i < OhlcList.size(); i++)
            {
                OhlcList.get(i).setClose((OhlcList.get(i).getHigh().add(OhlcList.get(i).getLow()).add(OhlcList.get(i).getClose())).divide(BigDecimal.valueOf(3L), MathContext.DECIMAL64));

                totalAverage = totalAverage.add(OhlcList.get(i).getClose());
                totalSquares = totalSquares.add(OhlcList.get(i).getClose().pow(2));

                if (i >= Period - 1)
                {
                    BigDecimal average = totalAverage.divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
                    BigDecimal stdev = BigDecimal.valueOf(Math.sqrt(totalSquares
                            .subtract(totalAverage.pow(2)
                                    .divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64)
                                    .divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64))
                            .doubleValue()));

                    bollingerBandSerie.MidBand.add(average);
                    BigDecimal up = average.add(BigDecimal.valueOf(Factor).multiply(stdev));
                    bollingerBandSerie.UpperBand.add(up);
                    BigDecimal down = average.subtract(BigDecimal.valueOf(Factor).multiply(stdev));
                    bollingerBandSerie.LowerBand.add(down);
                    BigDecimal bandWidth = (up.subtract(down)).divide(average, MathContext.DECIMAL64);
                    bollingerBandSerie.BandWidth.add(bandWidth);
                    BigDecimal bPercent = (OhlcList.get(i).getClose().subtract(down))
                            .divide(up.subtract(down), MathContext.DECIMAL64);
                    bollingerBandSerie.BPercent.add(bPercent);

                    totalAverage = totalAverage.subtract(OhlcList.get(i - Period + 1).getClose());
                    totalSquares = totalSquares.subtract(OhlcList.get(i - Period + 1).getClose().pow(2));
                }
                else
                {
                    bollingerBandSerie.MidBand.add(null);
                    bollingerBandSerie.UpperBand.add(null);
                    bollingerBandSerie.LowerBand.add(null);
                    bollingerBandSerie.BandWidth.add(null);
                    bollingerBandSerie.BPercent.add(null);
                }
            }

            return bollingerBandSerie;
        }
    }
