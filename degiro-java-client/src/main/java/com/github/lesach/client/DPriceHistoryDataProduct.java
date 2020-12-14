package com.github.lesach.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.lesach.client.engine.json.LocalDateTimeDeserializer;
import com.github.lesach.client.engine.json.LocalTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class DPriceHistoryDataProduct
{
    /*
        "issueId": 360017068,
        "companyId": 8,
        "name": "Renault",
        "identifier": "issueid:360017068",
        "isin": "FR0000131906",
        "alfa": "PRNL",
        "market": "XPAR",
        "currency": "EUR",
        "type": "AAN",
        "quality": "REALTIME",
        "lastPrice": 31.37,
        "lastTime": "2020-11-18T17:35:24",
        "absDiff": 1.41,
        "relDiff": 0.04706,
        "highPrice": 31.48,
        "highTime": "2020-11-18T16:15:17",
        "lowPrice": 29.94,
        "lowTime": "2020-11-18T09:52:14",
        "openPrice": 30.025,
        "openTime": "2020-11-18T09:00:15",
        "closePrice": 30.025,
        "closeTime": "2020-11-18T09:00:15",
        "cumulativeVolume": 2794704.0,
        "previousClosePrice": 29.96,
        "previousCloseTime": "2020-11-17T17:40:00",
        "tradingStartTime": "09:00:00",
        "tradingEndTime": "17:40:00",
        "tradingAddedTime": "00:10:00",
        "lowPriceP1Y": 12.77,
        "highPriceP1Y": 45.065,
        "windowStart": "2020-07-03T00:00:00",
        "windowEnd": "2020-11-14T00:00:00",
        "windowFirst": "2020-07-03T00:00:00",
        "windowLast": "2020-11-13T00:00:00",
        "windowHighTime": "2020-11-13T00:00:00",
        "windowHighPrice": 29.02,
        "windowLowTime": "2020-07-31T00:00:00",
        "windowLowPrice": 20.055,
        "windowOpenTime": "2020-07-03T00:00:00",
        "windowOpenPrice": 22.615,
        "windowPreviousCloseTime": "2020-07-02T00:00:00",
        "windowPreviousClosePrice": 22.605,
        "windowTrend": 0.2837867728378677283786772838
     */
    private Integer issueId;
    private Integer companyId;
    private String name;
    private String identifier;
    private String isin;
    private String alfa;
    private String market;
    private String currency;
    private String type;
    private String quality;
    private BigDecimal lastPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastTime;
    private BigDecimal absDiff;
    private BigDecimal relDiff;
    private BigDecimal highPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime highTime;
    private BigDecimal lowPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lowTime;
    private BigDecimal openPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime openTime;
    private String closePrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime closeTime;
    private BigDecimal cumulativeVolume;
    private BigDecimal previousClosePrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime previousCloseTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime tradingStartTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime tradingEndTime;
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime tradingAddedTime;
    private BigDecimal lowPriceP1Y;
    private BigDecimal highPriceP1Y;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowStart;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowEnd;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowFirst;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowLast;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowHighTime;
    private BigDecimal windowHighPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowLowTime;
    private BigDecimal windowLowPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowOpenTime;
    private BigDecimal windowOpenPrice;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime windowPreviousCloseTime;
    private BigDecimal windowPreviousClosePrice;
    private BigDecimal windowTrend;
}
