package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

public class ZLEMA extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        protected int Period = 10;

        public ZLEMA()
        { 
        
        }

        public ZLEMA(int period)
        {
            this.Period = period;
        }

        @Override
        public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie zlemaSerie = new SingleDoubleSerie();

            BigDecimal ratio = BigDecimal.valueOf(2L).divide(BigDecimal.valueOf(Period + 1), MathContext.DECIMAL64);
            BigDecimal lag = BigDecimal.ONE.divide(ratio, MathContext.DECIMAL64);
            BigDecimal wt = lag.subtract(lag); //DMOD( lag, 1.0D0 )
            BigDecimal meanOfFirstPeriod = OhlcList.stream().limit(Period).map(x -> x.Close).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64);
            
            for (int i = 0; i < OhlcList.size(); i++)
            {   
                if (i > Period - 1)
                {
                    int loc = BigDecimal.valueOf(i).subtract(lag).toBigInteger().intValue();
                    BigDecimal zlema = ratio
                            .multiply(BigDecimal.valueOf(2L)
                                    .multiply(OhlcList.get(i).Close)
                                    .subtract((OhlcList.get(loc).Close.multiply(BigDecimal.ONE.subtract(wt).add(OhlcList.get(loc + 1).Close.multiply(wt)))
                                    )))
                            .add(BigDecimal.ONE.subtract(ratio).multiply(zlemaSerie.Values.get(i - 1)));
                    zlemaSerie.Values.add(zlema);
                }
                else if (i == Period - 1)
	            {
                    zlemaSerie.Values.add(meanOfFirstPeriod);
	            }
                else
                {
                    zlemaSerie.Values.add(null);
                }
            }

            return zlemaSerie;
        }
    }
