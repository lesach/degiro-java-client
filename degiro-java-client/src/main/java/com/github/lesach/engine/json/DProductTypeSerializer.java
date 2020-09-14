package com.github.lesach.engine.json;

import com.github.lesach.DProductType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DProductTypeSerializer extends StdSerializer<DProductType> {
    protected DProductTypeSerializer() {
        super(DProductType.class);
    }

    @Override
    public void serialize(
            DProductType value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
