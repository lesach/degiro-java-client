package com.github.lesach.client.engine.json;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.lesach.client.DPriceHistory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lesach.client.DPriceHistoryDataProduct;
import com.github.lesach.client.DPriceHistorySerieData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class DPriceHistorySerieDataDeserializer extends JsonDeserializer<DPriceHistorySerieData> {

    @Override
    public DPriceHistorySerieData deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        DPriceHistorySerieData data = new DPriceHistorySerieData();
        ObjectMapper mapper = new ObjectMapper();
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
            data.setProduct(mapper.readValue(jp, DPriceHistoryDataProduct.class));
        } else {
            data.setPrices(mapper.readValue(jp, new TypeReference<List<BigDecimal[]>>(){}));
        }
        return data;
    }
}
