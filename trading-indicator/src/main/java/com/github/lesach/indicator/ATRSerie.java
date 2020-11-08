package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ATRSerie implements IIndicatorSerie
    {
        public List<BigDecimal> TrueHigh;
        public List<BigDecimal> TrueLow;
        public List<BigDecimal> TrueRange;
        public List<BigDecimal> ATR;

        public ATRSerie()
        {
            TrueHigh = new ArrayList<BigDecimal>();
            TrueLow = new ArrayList<BigDecimal>();
            TrueRange = new ArrayList<BigDecimal>();
            ATR = new ArrayList<BigDecimal>();
        }
    }
