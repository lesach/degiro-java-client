package com.github.lesach.indicator;

    public class Volume extends IndicatorCalculatorBase<SingleDoubleSerie>
    {
        @Override
        public  SingleDoubleSerie Calculate()
        {
            SingleDoubleSerie volumeSerie = new SingleDoubleSerie();

            for (Ohlc item : OhlcList)
            {
                volumeSerie.Values.add(item.Volume);
            }

            return volumeSerie;
        }
    }
