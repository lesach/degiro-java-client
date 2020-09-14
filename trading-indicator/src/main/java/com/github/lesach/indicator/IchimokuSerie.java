package com.github.lesach.indicator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IchimokuSerie implements IIndicatorSerie
{
    public List<BigDecimal> ConversionLine;
    public List<BigDecimal> BaseLine;
    public List<BigDecimal> LeadingSpanA;
    public List<BigDecimal> LeadingSpanB;
    public List<BigDecimal> LaggingSpan;

    public IchimokuSerie()
    {
        ConversionLine = new ArrayList<BigDecimal>();
        BaseLine = new ArrayList<BigDecimal>();
        LeadingSpanA = new ArrayList<BigDecimal>();
        LeadingSpanB = new ArrayList<BigDecimal>();
        LaggingSpan = new ArrayList<java.math.BigDecimal>();
    }
}
