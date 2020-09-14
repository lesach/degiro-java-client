package com.github.lesach.engine.json;

import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.lesach.DPriceHistory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lesach.DPriceHistoryDataProduct;
import com.github.lesach.DPriceHistorySerieData;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class DPriceHistorySerieDataDeserializer extends StdDeserializer<DPriceHistorySerieData> {
    protected DPriceHistorySerieDataDeserializer() {
        super(DPriceHistory.class);
    }

    @Override
    public DPriceHistorySerieData deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        DPriceHistorySerieData data = new DPriceHistorySerieData();
        ObjectMapper mapper = new ObjectMapper();
        if (jp.getCurrentToken() == JsonToken.START_OBJECT) {
            data.product = mapper.readValue(jp, DPriceHistoryDataProduct.class);
        } else {
            data.prices = mapper.readValue(jp, new TypeReference<List<BigDecimal[]>>(){});
        }
        return data;
    }
}
