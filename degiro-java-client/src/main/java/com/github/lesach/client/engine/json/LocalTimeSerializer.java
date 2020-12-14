package com.github.lesach.client.engine.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer  extends StdSerializer<LocalTime> {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    protected LocalTimeSerializer() {
        super(LocalTime.class);
    }

    @Override
    public void serialize(
            LocalTime value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        if (value == null)
            jgen.writeNull();
        else
            jgen.writeString(value.format(dateTimeFormatter));
    }
}
