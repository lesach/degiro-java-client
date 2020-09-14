package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AroonSerie implements IIndicatorSerie
    {
        public List<BigDecimal> Up;
        public List<BigDecimal> Down;

        public AroonSerie()
        {
            Up = new ArrayList<BigDecimal>();
            Down = new ArrayList<BigDecimal>();
        }
    }
