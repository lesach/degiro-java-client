package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.List;

    /// <summary>
    /// Moving Average Envelopes
    /// </summary>
    public class Envelope extends IndicatorCalculatorBase<EnvelopeSerie>
    {
        protected int Period = 20;
        protected BigDecimal Factor = BigDecimal.valueOf(0.025D);

        public Envelope()
        { 
        
        }

        public Envelope(int period, BigDecimal factor)
        {
            this.Period = period;
            this.Factor = factor;
        }

        /// <summary>
        /// Upper Envelope: 20-day SMA + (20-day SMA x .025)
        /// Lower Envelope: 20-day SMA - (20-day SMA x .025)
        /// </summary>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:moving_average_envelopes"/>
        /// <returns></returns>
        @Override
public  EnvelopeSerie Calculate()
        {
            EnvelopeSerie envelopeSerie = new EnvelopeSerie();

            SMA sma = new SMA(Period);
            sma.Load(OhlcList);
            List<BigDecimal> smaList = sma.Calculate().Values;

            for (int i = 0; i < OhlcList.size(); i++)
            {   
                if (smaList.get(i)  != null)
                {
                    envelopeSerie.Lower.add(smaList.get(i).subtract((smaList.get(i).multiply(Factor))));
                    envelopeSerie.Upper.add(smaList.get(i).add((smaList.get(i).multiply(Factor))));
                }
                else
                {
                    envelopeSerie.Lower.add(null);
                    envelopeSerie.Upper.add(null);
                }
            }

            return envelopeSerie;
        }
    }
