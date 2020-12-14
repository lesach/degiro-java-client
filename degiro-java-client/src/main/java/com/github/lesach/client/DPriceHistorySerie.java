package com.github.lesach.client;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.lesach.client.engine.json.DPriceHistorySerieDataDeserializer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DPriceHistorySerie
{
    private String times;
    private String expires;
    @JsonDeserialize(using = DPriceHistorySerieDataDeserializer.class)
    private DPriceHistorySerieData data;
    private String id;
    private String type;
}
