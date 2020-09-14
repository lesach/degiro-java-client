package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RSISerie implements IIndicatorSerie
{
    public List<BigDecimal> RSI;
    public List<BigDecimal> RS;

    public RSISerie()
    {
        RSI = new ArrayList<BigDecimal>();
        RS = new ArrayList<BigDecimal>();
    }
}
