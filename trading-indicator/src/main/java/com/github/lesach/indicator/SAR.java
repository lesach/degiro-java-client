package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SAR extends IndicatorCalculatorBase<SingleDoubleSerie>
{
    protected BigDecimal AccelerationFactor = BigDecimal.valueOf(0.02D);
    protected BigDecimal MaximumAccelerationFactor = BigDecimal.valueOf(0.2D);

    public SAR()
    {

    }

    public SAR(BigDecimal accelerationFactor, BigDecimal maximumAccelerationFactor)
    {
        this.AccelerationFactor = accelerationFactor;
        this.MaximumAccelerationFactor = maximumAccelerationFactor;
    }

    @Override
    public  SingleDoubleSerie Calculate()
    {
        SingleDoubleSerie sarSerie = new SingleDoubleSerie();

        // Difference of High and Low
        List<BigDecimal> differences = new ArrayList<>();
        for (Ohlc ohlc : OhlcList) {
            BigDecimal difference = ohlc.getHigh().subtract(ohlc.getLow());
            differences.add(difference);
        }

        // STDEV of differences
        BigDecimal stDev = Statistics.StandardDeviation(differences);

        BigDecimal[] sarArr = new BigDecimal[OhlcList.size()];

        BigDecimal[] highList = OhlcList.stream().map(x -> x.getHigh()).toArray(BigDecimal[]::new);
        BigDecimal[] lowList = OhlcList.stream().map(x -> x.getLow()).toArray(BigDecimal[]::new);

        /* Find first non-NA value */
        int beg = 1;
        for (int i = 0; i < OhlcList.size(); i++)
        {
            if (OhlcList.get(i).getHigh().compareTo(BigDecimal.ZERO) == 0 || OhlcList.get(i).getLow().compareTo(BigDecimal.ZERO) == 0)
            {
                sarArr[i] = BigDecimal.ZERO;
                beg++;
            }
            else
            {
                break;
            }
        }
        // TODO: needs attention, understand better and replace variable names with meaningful ones.
        /* Initialize values needed by the routine */
        int sig0 = 1, sig1 = 0;
        BigDecimal xpt0 = highList[beg - 1], xpt1;
        BigDecimal af0 = AccelerationFactor, af1;
        BigDecimal lmin, lmax;
        sarArr[beg - 1] = lowList[beg - 1].subtract(stDev);

        for (int i = beg; i < OhlcList.size(); i++)
        {
            /* Increment signal, extreme point, and acceleration factor */
            sig1 = sig0;
            xpt1 = xpt0;
            af1 = af0;

            /* Local extrema */
            lmin = (lowList[i - 1].compareTo(lowList[i]) > 0) ? lowList[i] : lowList[i - 1];
            lmax = (highList[i - 1].compareTo(highList[i]) > 0) ? highList[i - 1] : highList[i];
            /* Create signal and extreme price vectors */
            if (sig1 == 1)
            {  /* Previous buy signal */
                sig0 = (lowList[i].compareTo(sarArr[i - 1]) > 0) ? 1 : -1;  /* New signal */
                xpt0 = lmax.max(xpt1);             /* New extreme price */
            }
            else
            {           /* Previous sell signal */
                sig0 = (highList[i].compareTo(sarArr[i - 1]) < 0) ? -1 : 1;  /* New signal */
                xpt0 = lmin.min(xpt1);             /* New extreme price */
            }

            /*
                * Calculate acceleration factor (af)
                * and stop-and-reverse (sar) vector
            */

            /* No signal change */
            if (sig0 == sig1)
            {
                /* Initial calculations */
                sarArr[i] = sarArr[i - 1].add(xpt1.subtract(sarArr[i - 1]).multiply(af1));
                af0 = (af1.equals(MaximumAccelerationFactor)) ? MaximumAccelerationFactor : AccelerationFactor.add(af1);
                /* Current buy signal */
                if (sig0 == 1)
                {
                    af0 = (xpt0.compareTo(xpt1) > 0) ? af0 : af1;  /* Update acceleration factor */
                    sarArr[i] = (sarArr[i].compareTo(lmin) > 0) ? lmin : sarArr[i];  /* Determine sar value */
                }
                /* Current sell signal */
                else
                {
                    af0 = (xpt0.compareTo(xpt1) < 0) ? af0 : af1;  /* Update acceleration factor */
                    sarArr[i] = (sarArr[i].compareTo(lmax) > 0) ? sarArr[i] : lmax;   /* Determine sar value */
                }
            }
            else /* New signal */
            {
                af0 = AccelerationFactor;    /* reset acceleration factor */
                sarArr[i] = xpt0;  /* set sar value */
            }
        }

        sarSerie.Values = Arrays.asList(sarArr.clone());

        return sarSerie;
    }
}
