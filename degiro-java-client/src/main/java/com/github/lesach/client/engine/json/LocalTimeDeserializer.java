package com.github.lesach.client.engine.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {
    protected LocalTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return LocalTime.parse(jp.readValueAs(String.class), DateTimeFormatter.ofPattern("hh:mm:ss"));
    }
}
