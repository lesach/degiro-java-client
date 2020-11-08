package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ADXSerie implements IIndicatorSerie
{
    public List<BigDecimal> TrueRange;
    public List<BigDecimal> DINegative;
    public List<BigDecimal> DIPositive;
    public List<BigDecimal> DX;
    public List<BigDecimal> ADX;

    public ADXSerie()
    {
        TrueRange = new ArrayList<BigDecimal>();
        DINegative = new ArrayList<BigDecimal>();
        DIPositive = new ArrayList<BigDecimal>();
        DX = new ArrayList<BigDecimal>();
        ADX = new ArrayList<BigDecimal>();
    }
}

