package com.github.lesach.client.engine.json;

import com.github.lesach.client.DOrderAction;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DOrderActionDeserializer extends StdDeserializer<DOrderAction> {
    public DOrderActionDeserializer() {
        super(DOrderAction.class);
    }

    @Override
    public DOrderAction deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return DConvert.getDOrderAction(jp.readValueAs(String.class));
    }
}
