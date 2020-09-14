package com.github.lesach.engine.json;

import com.github.lesach.DProductType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DProductTypeDeserializer extends StdDeserializer<DProductType> {

    protected DProductTypeDeserializer() {
        super(DProductType.class);
    }

    @Override
    public DProductType deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return DConvert.getDProductType(jp.readValueAs(Long.class));
    }
}
