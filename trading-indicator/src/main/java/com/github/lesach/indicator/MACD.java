package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MACD extends IndicatorCalculatorBase<MACDSerie>
{
    protected int Fast = 12;
    protected int Slow = 26;
    protected int Signal = 9;
    protected boolean Percent = false;

    public MACD()
    { 
        
    }

    public MACD(boolean percent)
    {
        this.Percent = percent;
    }

    public MACD(int fast, int slow, int signal)
    {
        this.Fast = fast;
        this.Slow = slow;
        this.Signal = signal;
    }

    public MACD(int fast, int slow, int signal, boolean percent)
    {
        this.Fast = fast;
        this.Slow = slow;
        this.Signal = signal;
        this.Percent = percent;
    }

    @Override
    public  MACDSerie Calculate()
    {
        MACDSerie macdSerie = new MACDSerie();
        
        EMA ema = new EMA(Fast, false);
        ema.Load(OhlcList);
        List<BigDecimal> fastEmaValues = ema.Calculate().Values;

        ema = new EMA(Slow, false);
        ema.Load(OhlcList);
        List<BigDecimal> slowEmaValues = ema.Calculate().Values;

        for (int i = 0; i < OhlcList.size(); i++)
        {
            // MACD Line
            if (fastEmaValues.get(i)  != null && slowEmaValues.get(i)  != null)
            {   
                if (!Percent)
                {
                    macdSerie.MACDLine.add(fastEmaValues.get(i).subtract(slowEmaValues.get(i)));
                }
                else
                {
                    // macd <- 100 * ( mavg.fast / mavg.slow - 1 )
                    macdSerie.MACDLine.add(BigDecimal.valueOf(100)
                            .multiply(fastEmaValues.get(i)
                                    .divide(slowEmaValues.get(i), MathContext.DECIMAL64).subtract(BigDecimal.ONE)));
                }
                OhlcList.get(i).setClose(macdSerie.MACDLine.get(i));
            }
            else
            {
                macdSerie.MACDLine.add(null);
                OhlcList.get(i).setClose(BigDecimal.ZERO);
            }
        }
            
        long zeroCount = macdSerie.MACDLine.stream().filter(Objects::isNull).count();
        ema = new EMA(Signal, false);
        ema.Load(OhlcList.stream().skip(zeroCount).collect(Collectors.toList()));
        SingleDoubleSerie signalEmaValues = ema.Calculate();
        for (int i = 0; i < zeroCount; i++)
        {
            signalEmaValues.Values.add(0, null);
        }

        // Fill Signal and MACD Histogram lists
        for (int i = 0; i < signalEmaValues.Values.size(); i++)
        {
            macdSerie.Signal.add(signalEmaValues.Values.get(i));

            macdSerie.MACDHistogram.add(macdSerie.MACDLine.get(i).multiply(macdSerie.Signal.get(i)));
        }

        return macdSerie;
    }
}
