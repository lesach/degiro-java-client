package com.github.lesach.engine.json;

import com.github.lesach.DOrderType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DOrderTypeDeserializer extends StdDeserializer<DOrderType> {
    protected DOrderTypeDeserializer() {
        super(DOrderType.class);
    }

    @Override
    public DOrderType deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return DConvert.getDOrderType(jp.readValueAs(String.class));
    }
}
