package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ADX extends IndicatorCalculatorBase<ADXSerie>
{

    protected int Period = 14;

    public ADX()
    {
        super();
    }

    public ADX(int period)
    {
        this.Period = period;
    }

    @Override
    public  ADXSerie Calculate()
    {
        ADXSerie adxSerie = new ADXSerie();

        List<Ohlc> tempOhlcList = new ArrayList<Ohlc>();
        for (int i = 0; i < OhlcList.size(); i++)
        {
            int finalI = i;
            Ohlc tempOhlc = new Ohlc() {{ setClose(OhlcList.get(finalI).getHigh()); }};
            tempOhlcList.add(tempOhlc);
        }
        Momentum momentum = new Momentum();
        momentum.Load(tempOhlcList);
        List<BigDecimal> highMomentums = momentum.Calculate().Values;

        tempOhlcList = new ArrayList<Ohlc>();
        for (int i = 0; i < OhlcList.size(); i++)
        {
            int finalI = i;
            Ohlc tempOhlc = new Ohlc() {{ setClose(OhlcList.get(finalI).getLow()); }};
            tempOhlcList.add(tempOhlc);
        }
        momentum = new Momentum();
        momentum.Load(tempOhlcList);
        List<BigDecimal> lowMomentums = momentum.Calculate().Values;
        for (int i = 0; i < lowMomentums.size(); i++)
        {
            if (lowMomentums.get(i)  != null)
            {
                lowMomentums.set(i, lowMomentums.get(i).multiply(BigDecimal.valueOf(-1L)));
            }
        }

        //DMIp <- ifelse( dH==dL | (dH< 0 & dL< 0), 0, ifelse( dH >dL, dH, 0 ) )
        List<BigDecimal> DMIPositives = new ArrayList<BigDecimal>() {{ add(null); }};
        // DMIn <- ifelse( dH==dL | (dH< 0 & dL< 0), 0, ifelse( dH <dL, dL, 0 ) )
        List<BigDecimal> DMINegatives = new ArrayList<BigDecimal>() {{ add(null); }};
        for (int i = 1; i < OhlcList.size(); i++)
        {
            if (highMomentums.get(i).equals(lowMomentums.get(i)) || (highMomentums.get(i).compareTo(BigDecimal.ZERO) < 0 & lowMomentums.get(i).compareTo(BigDecimal.ZERO) < 0))
            {
                DMIPositives.add(BigDecimal.ZERO);
            }
            else
            {
                if (highMomentums.get(i).compareTo(lowMomentums.get(i)) > 0)
                {
                    DMIPositives.add(highMomentums.get(i));
                }
                else
                {
                    DMIPositives.add(BigDecimal.ZERO);
                }
            }

            if (highMomentums.get(i).equals(lowMomentums.get(i)) || (highMomentums.get(i).compareTo(BigDecimal.ZERO) < 0 & lowMomentums.get(i).compareTo(BigDecimal.ZERO) < 0))
            {
                DMINegatives.add(BigDecimal.ZERO);
            }
            else
            {
                if (highMomentums.get(i).compareTo(lowMomentums.get(i)) < 0)
                {
                    DMINegatives.add(lowMomentums.get(i));
                }
                else
                {
                    DMINegatives.add(BigDecimal.ZERO);
                }
            }
        }

        ATR atr = new ATR();
        atr.Load(OhlcList);
        List<BigDecimal> trueRanges = atr.Calculate().TrueRange;
        adxSerie.TrueRange = trueRanges;

        List<BigDecimal> trSum = wilderSum(trueRanges);

        // DIp <- 100 * wilderSum(DMIp, n=n) / TRsum
        List<BigDecimal> DIPositives = new ArrayList<BigDecimal>();
        List<BigDecimal> wilderSumOfDMIp = wilderSum(DMIPositives);
        for (int i = 0; i < wilderSumOfDMIp.size(); i++)
        {
            if (wilderSumOfDMIp.get(i)  != null)
            {
                DIPositives.add(wilderSumOfDMIp.get(i).multiply(BigDecimal.valueOf(100L).divide(trSum.get(i), MathContext.DECIMAL64)));
            }
            else
            {
                DIPositives.add(null);
            }
        }
        adxSerie.DIPositive = DIPositives;

        // DIn <- 100 * wilderSum(DMIn, n=n) / TRsum
        List<BigDecimal> DINegatives = new ArrayList<BigDecimal>();
        List<BigDecimal> wilderSumOfDMIn = wilderSum(DMINegatives);
        for (int i = 0; i < wilderSumOfDMIn.size(); i++)
        {
            if (wilderSumOfDMIn.get(i)  != null)
            {
                DINegatives.add(wilderSumOfDMIn.get(i).multiply(BigDecimal.valueOf(100L).divide(trSum.get(i), MathContext.DECIMAL64)));
            }
            else
            {
                DINegatives.add(null);
            }
        }
        adxSerie.DINegative = DINegatives;

        // DX  <- 100 * ( abs(DIp - DIn) / (DIp + DIn) )
        List<BigDecimal> DX = new ArrayList<BigDecimal>();
        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (DIPositives.get(i)  != null)
            {
                BigDecimal dx = BigDecimal.valueOf(100L).multiply(DIPositives.get(i).subtract(DINegatives.get(i)).abs().divide(DIPositives.get(i).add(DINegatives.get(i)), MathContext.DECIMAL64));
                DX.add(dx);
            }
            else
            {
                DX.add(null);
            }
        }
        adxSerie.DX = DX;

        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (DX.get(i)  != null)
            {
                OhlcList.get(i).setClose(DX.get(i));
            }
            else
            {
                OhlcList.get(i).setClose(BigDecimal.ZERO);
            }
        }

        EMA ema = new EMA(Period, true);
        ema.Load(OhlcList.stream().skip(Period).collect(Collectors.toList()));
        List<BigDecimal> emaValues = ema.Calculate().Values;
        for (int i = 0; i < Period; i++)
        {
            emaValues.add(0, null);
        }
        adxSerie.ADX = emaValues;

        return adxSerie;
    }

    private List<BigDecimal> wilderSum(List<BigDecimal> values)
    {
        List<BigDecimal> wilderSumsArray = new ArrayList<>(values.size());
        int beg = Period - 1;
        BigDecimal sum = BigDecimal.ZERO;
        int i = 0;
        for (i = 0; i < beg; i++)
        {
            /* Account for leading NAs : input */
            if (values.get(i) == null)
            {
                wilderSumsArray.set(i, null);
                beg++;
                wilderSumsArray.set(beg, BigDecimal.ZERO);
                continue;
            }
            /* Set leading NAs : output */
            if (i < beg)
            {
                wilderSumsArray.set(i, null);
            }
            /* Calculate raw sum to start */
            sum = sum.add(values.get(i));
        }

        wilderSumsArray.set(beg, values.get(i).add(sum.multiply(BigDecimal.valueOf(Period - 1)).divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64)));

        /* Loop over non-NA input values */
        for (i = beg + 1; i < values.size(); i++)
        {
            wilderSumsArray.set(i, values.get(i).add(wilderSumsArray.get(i - 1).multiply(BigDecimal.valueOf(Period - 1)).divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64)));
        }

        return wilderSumsArray;
    }
}
