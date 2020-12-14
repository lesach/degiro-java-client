package com.github.lesach.strategy.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.lesach.strategy.strategy.StrategyStepConditionParameter;

import java.io.IOException;
import java.math.BigDecimal;

public class StrategyStepConditionParameterDeserializer extends StdDeserializer<StrategyStepConditionParameter> {

    public StrategyStepConditionParameterDeserializer() {
        super(StrategyStepConditionParameter.class);
    }

    @Override
    public StrategyStepConditionParameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

    }
}

}
