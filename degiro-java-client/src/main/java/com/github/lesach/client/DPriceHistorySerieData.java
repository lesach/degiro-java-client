package com.github.lesach.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class DPriceHistorySerieData
{
    // [IgnoreDataMember]
    private List<BigDecimal[]> prices;
    // [IgnoreDataMember]
    private DPriceHistoryDataProduct product;
}
