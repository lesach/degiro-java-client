package com.github.lesach.client.engine.json;

import com.github.lesach.client.DOrderAction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DOrderActionSerializer  extends StdSerializer<DOrderAction> {
    public DOrderActionSerializer() {
        super(DOrderAction.class);
    }

    @Override
    public void serialize(
            DOrderAction value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeString(value.toString());
    }
}
