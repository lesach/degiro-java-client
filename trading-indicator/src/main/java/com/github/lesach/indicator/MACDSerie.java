package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MACDSerie implements IIndicatorSerie
    {
        public List<BigDecimal> MACDLine;
        public List<BigDecimal> MACDHistogram;
        public List<BigDecimal> Signal;

        public MACDSerie() 
        {
            this.MACDLine = new ArrayList<BigDecimal>();
            this.MACDHistogram = new ArrayList<BigDecimal>();
            this.Signal = new ArrayList<BigDecimal>();
        }
    }
