package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/// <summary>
    /// Chaikin Money Flow
    /// </summary>
    public class CMF extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period = 20;

        public CMF()
        { 
        
        }

        public CMF(int period)
        {
            this.Period = period;
        }

        /// <summary>
        /// Chaikin Money Flow
        /// Money Flow Multiplier = [(Close  -  Low) - (High - Close)] /(High - Low) 
        /// Money Flow Volume = Money Flow Multiplier x Volume for the Period
        /// 20-period CMF = 20-period Sum of Money Flow Volume / 20 period Sum of Volume
        /// </summary>
        /// <see cref="http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf"/>
        /// <returns></returns>
        @Override
public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie cmfSerie = new SingleDoubleSerie();

            List<BigDecimal> moneyFlowVolumeList = new ArrayList<BigDecimal>();

            for (int i = 0; i < OhlcList.size(); i++)
            {
                BigDecimal moneyFlowMultiplier = ((OhlcList.get(i).Close.subtract(OhlcList.get(i).Low)).subtract(OhlcList.get(i).High.subtract(OhlcList.get(i).Close)))
                        .divide((OhlcList.get(i).High.subtract(OhlcList.get(i).Low)), MathContext.DECIMAL64);
                
                moneyFlowVolumeList.add(moneyFlowMultiplier.multiply(OhlcList.get(i).Volume));

                if (i >= Period - 1)
                {
                    BigDecimal sumOfMoneyFlowVolume = BigDecimal.ZERO, sumOfVolume = BigDecimal.ZERO;
                    for (int j = i; j >= i - (Period - 1); j--)
                    {
                        sumOfMoneyFlowVolume = sumOfMoneyFlowVolume.add(moneyFlowVolumeList.get(j));
                        sumOfVolume = sumOfVolume.add(OhlcList.get(j).Volume);
                    }
                    cmfSerie.Values.add(sumOfMoneyFlowVolume.divide(sumOfVolume, MathContext.DECIMAL64));
                }
                else
                {
                    cmfSerie.Values.add(null);
                }
            }

            return cmfSerie;
        }
    }
