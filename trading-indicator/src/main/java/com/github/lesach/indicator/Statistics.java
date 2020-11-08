package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Statistics
{
    public static BigDecimal StandardDeviation(List<BigDecimal> valueList)
    {
        BigDecimal M = BigDecimal.ZERO;
        BigDecimal S = BigDecimal.ZERO;
        BigDecimal k = BigDecimal.ONE;
        for (BigDecimal value : valueList)
        {
            BigDecimal tmpM = M;
            M = M.add(value.subtract(tmpM).divide(k, RoundingMode.HALF_UP));
            S = S.add(value.subtract(tmpM).multiply(value.subtract(M)));
            k = k.add(BigDecimal.ONE);
        }
        return S.divide(k.subtract(BigDecimal.valueOf(2L)), RoundingMode.HALF_UP).sqrt(MathContext.DECIMAL64);
    }

    public static List<BigDecimal> RunMax(List<BigDecimal> list, int period)
    {
        List<BigDecimal> maxList = new ArrayList<BigDecimal>();

        for (int i = 0; i < list.size(); i++)
        {
            if (i >= period - 1)
            {
                BigDecimal max = BigDecimal.ZERO;
                for (int j = i - (period - 1); j <= i; j++)
                {
                    if (list.get(j).compareTo(max) > 0)
                    {
                        max = list.get(j);
                    }
                }

                maxList.add(max);
            }
            else
            {
                maxList.add(null);
            }
        }

        return maxList;
    }

    public static List<BigDecimal> RunMin(List<BigDecimal> list, int period)
    {
        List<BigDecimal> minList = new ArrayList<BigDecimal>();

        for (int i = 0; i < list.size(); i++)
        {
            if (i >= period - 1)
            {
                BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
                for (int j = i - (period - 1); j <= i; j++)
                {
                    if (list.get(j).compareTo(min) < 0)
                    {
                        min = list.get(j);
                    }
                }

                minList.add(min);
            }
            else
            {
                minList.add(null);
            }
        }

        return minList;
    }
}
