package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SingleDoubleSerie implements IIndicatorSerie
{
    public List<BigDecimal> Values;

    public SingleDoubleSerie()
    {
        Values = new ArrayList<>();
    }
}
