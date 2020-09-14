package com.github.lesach.engine.json;

import com.github.lesach.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

public class DConvert
{

    static public ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        //module.addDeserializer(Item.class, new ItemDeserializer());
        module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        module.addDeserializer(DOrderAction.class, new DOrderActionDeserializer());
        module.addDeserializer(DOrderTime.class, new DOrderTimeDeserializer());
        module.addDeserializer(DOrderType.class, new DOrderTypeDeserializer());
        module.addDeserializer(DProductType.class, new DProductTypeDeserializer());
        module.addDeserializer(DPriceHistorySerieData.class, new DPriceHistorySerieDataDeserializer());
        module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
        module.addSerializer(DOrderAction.class, new DOrderActionSerializer());
        module.addSerializer(DOrderTime.class, new DOrderTimeSerializer());
        module.addSerializer(DOrderType.class, new DOrderTypeSerializer());
        module.addSerializer(DProductType.class, new DProductTypeSerializer());
        mapper.registerModule(module);
        return mapper;
    }

    static public DProductType getDProductType(long value)
    {
        switch ((int) value) {
            case 0: return DProductType.ALL;
            case 1: return DProductType.SHARES;
            case 2: return DProductType.BONDS;
            case 7: return DProductType.FUTURES;
            case 8: return DProductType.OPTIONS;
            case 13: return DProductType.INVESTMENT_FUNDS;
            case 14: return DProductType.LEVERAGED_PRODUCTS;
            case 131: return DProductType.ETF;
            case 535: return DProductType.CFD;
            case 536: return DProductType.WARRANTS;
            default: return DProductType.NULL;
        }
    }

    static public DOrderTime getDOrderTime(String value)
    {
        switch (value)
        {
            case "DAY": return DOrderTime.DAY;
            case "GTC": return DOrderTime.PERMANENT;
            default: return DOrderTime.NULL;
        }
    }

    static public DOrderTime getDOrderTime(int value)
    {
        switch (value)
        {
            case 0: return DOrderTime.DAY;
            case 1: return DOrderTime.PERMANENT;
            default: return DOrderTime.NULL;
        }
    }

    static public DOrderAction getDOrderAction(String value)
    {
        switch (value)
        {
            case "BUY": return DOrderAction.BUY;
            case "SELL": return DOrderAction.SELL;
            default: return DOrderAction.NULL;
        }
    }

    static public DOrderType getDOrderType(String value)
    {
        switch (value)
        {
            case "LIMIT": return DOrderType.LIMITED;
            case "MARKET": return DOrderType.MARKET_ORDER;
            case "STOPLOSS": return DOrderType.STOP_LOSS;
            case "STOPLIMIT": return DOrderType.LIMITED_STOP_LOSS;
            default: return DOrderType.NULL;
        }
    }

    static public DOrderType getDOrderType(int value)
    {
        switch (value)
        {
            case 0: return DOrderType.LIMITED;
            case 1: return DOrderType.MARKET_ORDER;
            case 2: return DOrderType.STOP_LOSS;
            case 3: return DOrderType.LIMITED_STOP_LOSS;
            default: return DOrderType.NULL;
        }
    }
}


