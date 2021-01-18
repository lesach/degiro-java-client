package com.github.lesach.strategy.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.client.engine.json.DConvert;
import com.github.lesach.strategy.serie.SerieKey;
import com.github.lesach.strategy.strategy.EStrategyStepConditionParameterType;
import com.github.lesach.strategy.strategy.StrategyStepConditionParameter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class StrategyStepConditionParameterDeserializer extends StdDeserializer<StrategyStepConditionParameter> {

    public static final DateTimeFormatter HM_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static ObjectMapper objectMapper = new ObjectMapper();

    public StrategyStepConditionParameterDeserializer() {
        super(StrategyStepConditionParameter.class);
    }


    @Override
    public StrategyStepConditionParameter deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        // {\"value\":null,\"name\":\"Serie A\",\"parameterType\":\"Serie\"}"
        //\"value\":{\"indicator\":{\"indicatorType\":\"EMA\",\"parameters\":[{\"name\":\"Period\",\"value\":\"25\"}]}}}"}
        JsonNode node = jp.getCodec().readTree(jp);
        StrategyStepConditionParameter result = new StrategyStepConditionParameter();
        result.setName(node.get("name").asText());
        result.setParameterType(EStrategyStepConditionParameterType.getByValue(node.get("parameterType").asText()));
        switch (result.getParameterType()) {
            case Time: result.setValue(LocalTime.parse(node.get("value").asText(), HM_FORMAT));
                break;
            case Number: result.setValue(BigDecimal.valueOf(Double.parseDouble(node.get("value").asText())));
                break;
            case Serie: result.setValue(objectMapper.treeToValue(node.get("value"), SerieKey.class));
                break;
            case Product: result.setValue(objectMapper.treeToValue(node.get("value"), DProductDescription.class));
                break;
        }
        return result;
    }
}
