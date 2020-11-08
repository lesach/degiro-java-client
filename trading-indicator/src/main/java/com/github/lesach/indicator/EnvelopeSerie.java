package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EnvelopeSerie implements IIndicatorSerie
    {
        public List<BigDecimal> Upper;
        public List<BigDecimal> Lower;

        public EnvelopeSerie()
        {
            Upper = new ArrayList<BigDecimal>();
            Lower = new ArrayList<BigDecimal>();
        }
    }
