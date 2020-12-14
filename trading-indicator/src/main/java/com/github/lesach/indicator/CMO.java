package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/// <summary>
    /// Chande Momentum Oscillator (CMO)
    /// </summary>
    public class CMO extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period = 14;

        public CMO() 
        { 
        
        }

        public CMO(int period)
        {
            this.Period = period;
        }

        /// <summary>
        /// Chande Momentum Oscillator (CMO)
        /// </summary>
        /// <see cref="http://www.fmlabs.com/reference/default.htm?url=CMO.htm"/>
        /// <returns></returns>
        @Override
public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie cmoSerie = new SingleDoubleSerie();
            cmoSerie.Values.add(null);

            List<BigDecimal> upValues = new ArrayList<>();
            upValues.add(BigDecimal.ZERO);
            List<BigDecimal> downValues = new ArrayList<>();
            downValues.add(BigDecimal.ZERO);

            for (int i = 1; i < OhlcList.size(); i++)
            {
                if (OhlcList.get(i).getClose().compareTo(OhlcList.get(i - 1).getClose()) > 0)
                {
                    upValues.add(OhlcList.get(i).getClose().subtract(OhlcList.get(i - 1).getClose()));
                    downValues.add(BigDecimal.ZERO);
                }
                else if (OhlcList.get(i).getClose().compareTo(OhlcList.get(i - 1).getClose()) < 0)
                {
                    upValues.add(BigDecimal.ZERO);
                    downValues.add(OhlcList.get(i - 1).getClose().subtract(OhlcList.get(i).getClose()));
                }
                else
                {
                    upValues.add(BigDecimal.ZERO);
                    downValues.add(BigDecimal.ZERO);
                }

                if (i >= Period)
                {
                    BigDecimal upTotal = BigDecimal.ZERO, downTotal = BigDecimal.ZERO;
                    for (int j = i; j >= i - (Period - 1); j--)
                    {
                        upTotal = upTotal.add(upValues.get(j));
                        downTotal = downTotal.add(downValues.get(j));
                    }

                    BigDecimal cmo = BigDecimal.valueOf(100).multiply((upTotal.subtract(downTotal)).divide((upTotal.add(downTotal)), MathContext.DECIMAL64));
                    cmoSerie.Values.add(cmo);
                }
                else
                {
                    cmoSerie.Values.add(null);
                }
            }

            return cmoSerie;
        }
    }
