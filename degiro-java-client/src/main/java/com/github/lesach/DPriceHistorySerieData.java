package com.github.lesach;

import java.math.BigDecimal;
import java.util.List;

// [DataContract]
public class DPriceHistorySerieData
{
    // [IgnoreDataMember]
    public List<BigDecimal[]> prices;
    // [IgnoreDataMember]
    public DPriceHistoryDataProduct product;
}
