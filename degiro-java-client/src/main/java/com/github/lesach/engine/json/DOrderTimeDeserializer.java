package com.github.lesach.engine.json;

import com.github.lesach.DOrderTime;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DOrderTimeDeserializer extends StdDeserializer<DOrderTime> {
    protected DOrderTimeDeserializer() {
        super(DOrderTime.class);
    }

    @Override
    public DOrderTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return DConvert.getDOrderTime(jp.readValueAs(String.class));
    }
}
