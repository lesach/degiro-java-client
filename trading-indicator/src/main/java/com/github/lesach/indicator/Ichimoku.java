package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ichimoku extends IndicatorCalculatorBase<IchimokuSerie>
    {
        protected int Fast = 9;
        protected int Med = 26;
        protected int Slow = 26;

        public Ichimoku() 
        { 
        
        }

        public Ichimoku(int fast, int med, int slow)
        {
            this.Fast = fast;
            this.Med = med;
            this.Slow = slow;
        }

        @Override
        public  IchimokuSerie Calculate()
        {
            IchimokuSerie ichimokuSerie = new IchimokuSerie();

            List<BigDecimal> highList = OhlcList.stream().map(x -> x.High).collect(Collectors.toList());
            List<BigDecimal> lowList = OhlcList.stream().map(x -> x.Low).collect(Collectors.toList());

            // TurningLine
            List<BigDecimal> runMaxFast = Statistics.RunMax(highList, Fast);
            List<BigDecimal> runMinFast = Statistics.RunMin(lowList, Fast);
            List<BigDecimal> runMaxMed = Statistics.RunMax(highList, Med);
            List<BigDecimal> runMinMed = Statistics.RunMin(lowList, Med);
            List<BigDecimal> runMaxSlow = Statistics.RunMax(highList, Slow);
            List<BigDecimal> runMinSlow = Statistics.RunMin(lowList, Slow);

            for (int i = 0; i < OhlcList.size(); i++)
            {   
                if (i >= Fast - 1)
                {
                    ichimokuSerie.ConversionLine.add((runMaxFast.get(i).add(runMinFast.get(i))).divide(BigDecimal.valueOf(2L), MathContext.DECIMAL64));
                }
                else
                {
                    ichimokuSerie.ConversionLine.add(null);
                }

                if (i >= Med - 1)
                {
                    ichimokuSerie.BaseLine.add(runMaxMed.get(i).add(runMinMed.get(i)).divide(BigDecimal.valueOf(2), MathContext.DECIMAL64));
                    ichimokuSerie.LeadingSpanA.add(ichimokuSerie.BaseLine.get(i).add(ichimokuSerie.ConversionLine.get(i)).divide(BigDecimal.valueOf(2), MathContext.DECIMAL64));
                }
                else
                {
                    ichimokuSerie.BaseLine.add(null);
                    ichimokuSerie.LeadingSpanA.add(null);
                }

                if (i >= Slow - 1)
                {
                    ichimokuSerie.LeadingSpanB.add(runMaxSlow.get(i).add(runMinSlow.get(i)).divide(BigDecimal.valueOf(2), MathContext.DECIMAL64));
                }
                else
                {
                    ichimokuSerie.LeadingSpanB.add(null);
                }
            }

            // shift to left Med
            List<BigDecimal> laggingSpan = new ArrayList<>();//OhlcList.Select(x => x.Close).ToList();//new BigDecimal[OhlcList.size()];
            for (int i = 0; i < OhlcList.size(); i++)
			{   
                laggingSpan.add(null);			    
			}
            for (int i = 0; i < OhlcList.size(); i++)
            {
                if (i >= Med - 1)
                {
                    laggingSpan.set(i - (Med - 1), OhlcList.get(i).Close);
                }
            }
            ichimokuSerie.LaggingSpan = laggingSpan;

            return ichimokuSerie;
        }
    }
