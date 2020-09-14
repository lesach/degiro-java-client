package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;

/// <summary>
    /// Detrended Price Oscillator (DPO)
    /// </summary>
    public class DPO extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
         protected int Period = 10;

        public DPO()
        { 
        
        }

        public DPO(int period)
        {
            this.Period = period;
        }

        /// <summary>
        /// Price {X/2 + 1} periods ago less the X-period simple moving average.
        /// X refers to the number of periods used to calculate the Detrended Price 
        /// Oscillator. A 20-day DPO would use a 20-day SMA that is displaced by 11 
        /// periods {20/2 + 1 = 11}. This displacement shifts the 20-day SMA 11 days 
        /// to the left, which actually puts it : the middle of the look-back
        /// period. The value of the 20-day SMA is then subtracted from the price 
        /// : the middle of this look-back period. In short, DPO(20) equals price
        /// 11 days ago less the 20-day SMA.  
        /// </summary>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:detrended_price_osci"/>
        /// <returns></returns>
        @Override
        public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie dpoSerie = new SingleDoubleSerie();

            SMA sma = new SMA(Period);
            sma.Load(OhlcList);
            List<BigDecimal> smaList = sma.Calculate().Values;
                
            // shift to left (n / 2) + 1
            for (int i = 0; i < smaList.size(); i++)
            {   
                if (i >= Period - 1)
                {
                    smaList.set(i - ((Period / 2) + 1), smaList.get(i));
                    smaList.set(i, null);
                }
            }

            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (smaList.get(i)  != null)
                {
                    BigDecimal dpo = OhlcList.get(i).Close.subtract(smaList.get(i));
                    dpoSerie.Values.add(dpo);
                }
                else
                {
                    dpoSerie.Values.add(null);
                }
            }

            return dpoSerie;
        }
    }
