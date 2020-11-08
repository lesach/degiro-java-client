package com.github.lesach.client.engine.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends StdDeserializer<BigDecimal> {

    protected BigDecimalDeserializer() {
        super(BigDecimal.class);
    }

    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        String value = jp.readValueAs(String.class);
        if (value == null)
            return null;
        else
            return new BigDecimal(value);
    }
}
