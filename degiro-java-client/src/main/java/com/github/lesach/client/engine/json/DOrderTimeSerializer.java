package com.github.lesach.client.engine.json;

import com.github.lesach.client.DOrderTime;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DOrderTimeSerializer extends StdSerializer<DOrderTime> {
    public DOrderTimeSerializer() {
        super(DOrderTime.class);
    }

    @Override
    public void serialize(
            DOrderTime value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
