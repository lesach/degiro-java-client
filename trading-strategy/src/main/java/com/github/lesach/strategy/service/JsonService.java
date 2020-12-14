package com.github.lesach.strategy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.lesach.client.*;
import com.github.lesach.client.engine.json.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class JsonService implements IJsonService{

    private final ObjectMapper json = new ObjectMapper() {{
            SimpleModule module = new SimpleModule();
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
            module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
            module.addDeserializer(DOrderAction.class, new DOrderActionDeserializer());
            module.addDeserializer(DOrderTime.class, new DOrderTimeDeserializer());
            module.addDeserializer(DOrderType.class, new DOrderTypeDeserializer());
            module.addDeserializer(DProductType.class, new DProductTypeDeserializer());
            module.addDeserializer(DPriceHistorySerieData.class, new DPriceHistorySerieDataDeserializer());
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            module.addSerializer(BigDecimal.class, new BigDecimalSerializer());
            module.addSerializer(DOrderAction.class, new DOrderActionSerializer());
            module.addSerializer(DOrderTime.class, new DOrderTimeSerializer());
            module.addSerializer(DOrderType.class, new DOrderTypeSerializer());
            module.addSerializer(DProductType.class, new DProductTypeSerializer());
            registerModule(module);
        }};

    @Override
    public String toJson(Object value) throws JsonProcessingException {
        return json.writeValueAsString(value);
    }

    @Override
    public <T> T fromJson(String value, Class<T> tClass) throws IOException {
        return json.readValue(value, tClass);
    }

    @Override
    public <T> T fromJson(String value, TypeReference<T> tClass) throws IOException {
        return json.readValue(value, tClass);
    }
}
