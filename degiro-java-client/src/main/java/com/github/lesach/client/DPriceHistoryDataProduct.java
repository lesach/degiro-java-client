package com.github.lesach.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

// [DataContract]
public class DPriceHistoryDataProduct
{
    // [DataMember(Name = "issueId")]
    public String issueId;
    // [DataMember(Name = "companyId")]
    public String companyId;
    // [DataMember(Name = "name")]
    public String name;
    // [DataMember(Name = "identifier")]
    public String identifier;
    // [DataMember(Name = "isin")]
    public String isin;
    // [DataMember(Name = "alfa")]
    public String alfa;
    // [DataMember(Name = "market")]
    public String market;
    // [DataMember(Name = "currency")]
    public String currency;
    // [DataMember(Name = "type")]
    public String type;
    // [DataMember(Name = "quality")]
    public String quality;
    // [DataMember(Name = "lastPrice")]
    public String lastPrice;
    // [DataMember(Name = "lastTime")]
    public LocalDateTime lastTime;
    // [DataMember(Name = "absDiff")]
    public String absDiff;
    // [DataMember(Name = "relDiff")]
    public String relDiff;
    // [DataMember(Name = "highPrice")]
    public String highPrice;
    // [DataMember(Name = "highTime")]
    public LocalDateTime highTime;
    // [DataMember(Name = "lowPrice")]
    public String lowPrice;
    // [DataMember(Name = "lowTime")]
    public LocalDateTime lowTime;
    // [DataMember(Name = "openPrice")]
    public String openPrice;
    // [DataMember(Name = "openTime")]
    public LocalDateTime openTime;
    // [DataMember(Name = "closePrice")]
    public String closePrice;
    // [DataMember(Name = "closeTime")]
    public LocalDateTime closeTime;
    // [DataMember(Name = "cumulativeVolume")]
    public BigDecimal cumulativeVolume;
    // [DataMember(Name = "previousClosePrice")]
    public BigDecimal previousClosePrice;
    // [DataMember(Name = "previousCloseTime")]
    public LocalDateTime previousCloseTime;
    // [DataMember(Name = "tradingStartTime")]
    public LocalTime tradingStartTime;
    // [DataMember(Name = "tradingEndTime")]
    public LocalTime tradingEndTime;
    // [DataMember(Name = "tradingAddedTime")]
    public LocalTime tradingAddedTime;
    // [DataMember(Name = "lowPriceP1Y")]
    public String lowPriceP1Y;
    // [DataMember(Name = "highPriceP1Y")]
    public String highPriceP1Y;
    // [DataMember(Name = "windowStart")]
    public LocalDateTime windowStart;
    // [DataMember(Name = "windowEnd")]
    public LocalDateTime windowEnd;
    // [DataMember(Name = "windowFirst")]
    public LocalDateTime windowFirst;
    // [DataMember(Name = "windowLast")]
    public LocalDateTime windowLast;
    // [DataMember(Name = "windowHighTime")]
    public LocalDateTime windowHighTime;
    // [DataMember(Name = "windowHighPrice")]
    public BigDecimal windowHighPrice;
    // [DataMember(Name = "windowLowTime")]
    public LocalDateTime windowLowTime;
    // [DataMember(Name = "windowLowPrice")]
    public BigDecimal windowLowPrice;
    // [DataMember(Name = "windowOpenTime")]
    public LocalDateTime windowOpenTime;
    // [DataMember(Name = "windowOpenPrice")]
    public BigDecimal windowOpenPrice;
    // [DataMember(Name = "windowPreviousCloseTime")]
    public LocalDateTime windowPreviousCloseTime;
    // [DataMember(Name = "windowPreviousClosePrice")]
    public BigDecimal windowPreviousClosePrice;
    // [DataMember(Name = "windowTrend")]
    public BigDecimal windowTrend;
}
