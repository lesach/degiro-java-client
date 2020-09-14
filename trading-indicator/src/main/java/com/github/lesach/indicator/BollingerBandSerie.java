package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BollingerBandSerie implements IIndicatorSerie
    {
        public List<BigDecimal> LowerBand;
        public List<BigDecimal> MidBand;
        public List<BigDecimal> UpperBand;
        public List<BigDecimal> BandWidth;
        public List<BigDecimal> BPercent;

        public BollingerBandSerie()
        {
            LowerBand = new ArrayList<BigDecimal>();
            MidBand = new ArrayList<BigDecimal>();
            UpperBand = new ArrayList<BigDecimal>();
            BandWidth = new ArrayList<BigDecimal>();
            BPercent = new ArrayList<BigDecimal>();
        }
    }
