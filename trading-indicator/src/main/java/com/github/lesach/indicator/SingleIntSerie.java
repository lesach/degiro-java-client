package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SingleIntSerie implements IIndicatorSerie
    {
        public List<BigDecimal> Values;

        public SingleIntSerie()
        {
            Values = new ArrayList<>();
        }
    }
