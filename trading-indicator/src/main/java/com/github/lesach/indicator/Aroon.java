package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;

/// <summary>
    /// Aroon
    /// </summary>
    public class Aroon extends IndicatorCalculatorBase<AroonSerie>
    {
        protected int Period;

        public Aroon(int period)
        {
            Period = period;
        }

        /// <summary>
        /// Aroon up: {((number of periods) - (number of periods since highest high)) / (number of periods)} x 100
        /// Aroon down: {((number of periods) - (number of periods since lowest low)) / (number of periods)} x 100
        /// </summary>
        /// <see cref="http://www.investopedia.com/ask/answers/112814/what-aroon-indicator-formula-and-how-indicator-calculated.asp"/>
        /// <returns></returns>
        @Override
        public  AroonSerie Calculate()
        {
            AroonSerie aroonSerie = new AroonSerie();
            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= Period)
                {
                    BigDecimal aroonUp = CalculateAroonUp(i);
                    BigDecimal aroonDown = CalculateAroonDown(i);

                    aroonSerie.Down.add(aroonDown);
                    aroonSerie.Up.add(aroonUp);    
                }
                else
                {
                    aroonSerie.Down.add(null);
                    aroonSerie.Up.add(null);
                }
            }

            return aroonSerie;
        }

        private BigDecimal CalculateAroonUp(int i)
        {
            int maxIndex = FindMax(i - Period, i);

            BigDecimal up = CalcAroon(i - maxIndex);

            return up;
        }

        private BigDecimal CalculateAroonDown(int i)
        {
            int minIndex = FindMin(i - Period, i);

            BigDecimal down = CalcAroon(i - minIndex);

            return down;
        }

        private BigDecimal CalcAroon(int numOfDays)
        {
            BigDecimal result = BigDecimal.valueOf(Period - numOfDays).multiply((BigDecimal.valueOf(100).divide(BigDecimal.valueOf(Period), MathContext.DECIMAL64)));
            return result;
        }

        private int FindMin(int startIndex, int endIndex)
        {
            BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
            int index = startIndex;
            for (int i = startIndex; i <= endIndex; i++)
            {
                if (min.compareTo(OhlcList.get(i).Low) < 0)
                    continue;

                min = OhlcList.get(i).Low;
                index = i;
            }
            return index;
        }

        private int FindMax(int startIndex, int endIndex)
        {
            BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
            int index = startIndex;
            for (int i = startIndex; i <= endIndex; i++)
            {
                if (max.compareTo(OhlcList.get(i).High) > 0)
                    continue;

                max = OhlcList.get(i).High;
                index = i;
            }
            return index;
        }
    }

