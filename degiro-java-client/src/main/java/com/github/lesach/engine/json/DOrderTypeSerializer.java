package com.github.lesach.engine.json;

import com.github.lesach.DOrderType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DOrderTypeSerializer extends StdSerializer<DOrderType> {
    protected DOrderTypeSerializer() {
        super(DOrderType.class);
    }

    @Override
    public void serialize(
            DOrderType value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
