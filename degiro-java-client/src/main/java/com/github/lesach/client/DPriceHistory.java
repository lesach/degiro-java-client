/*
 * Copyright 2018 casa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.lesach.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author indiketa
 */
@Getter
@Setter
public class DPriceHistory {

    private String requestid;
    private String start;
    private String end;
    private String resolution;
    private List<DPriceHistorySerie> series = null;

    public List<BigDecimal[]> getPrices() {
        DPriceHistorySerie priceHistorySerie = series.stream().filter(s -> s.getType().equals("time")).findFirst().orElse(null);
        if (priceHistorySerie != null)
            return priceHistorySerie.getData().getPrices();
        return null;
    }

    public DPriceHistoryDataProduct getProduct() {
        DPriceHistorySerie priceHistorySerie = series.stream().filter(s -> s.getType().equals("object")).findFirst().orElse(null);
        if (priceHistorySerie != null)
            return priceHistorySerie.getData().getProduct();
        return null;
    }

    static private final Map<String, Integer> resolutions = new HashMap<String, Integer>() {{
        put("PT1M", 60);
        put("PT1H", 3600);
        put("P1D", 86400);
    }};


    static private final DateTimeFormatter dateTimeFormatterHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    static private final DateTimeFormatter dateTimeFormatterHHmm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static private final Map<String, DateTimeFormatter> dateTimeFormatters = new HashMap<String, DateTimeFormatter>() {{
        put("PT1S", dateTimeFormatterHHmmss);
        put("PT1M", dateTimeFormatterHHmm);
        put("PT1H", dateTimeFormatterHHmm);
        put("P1D", dateTimeFormatter);
    }};

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatters.get(resolution);
    }

    /// <summary>
    /// Update prices history to have time in second
    /// </summary>
    public void NormalizeResolution()
    {
        if (!resolution.equals("PT1S"))
        {
            if (getPrices() != null)
            {
                int scaleFactor = resolutions.get(resolution);
                for(BigDecimal[] price : getPrices())
                    price[0] = price[0].multiply(BigDecimal.valueOf(scaleFactor));
            }
        }
    }

}
