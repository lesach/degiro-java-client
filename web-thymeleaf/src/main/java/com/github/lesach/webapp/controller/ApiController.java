package com.github.lesach.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.lesach.client.DPriceHistory;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.client.DProductSearch;
import com.github.lesach.client.DProductType;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.client.log.DLog;
import com.github.lesach.strategy.DeGiroClientInterface;
import com.github.lesach.strategy.EBooleanOperator;
import com.github.lesach.strategy.EPeriodInstantType;
import com.github.lesach.strategy.ESerieEventType;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.serie.IndicatorProviderInterface;
import com.github.lesach.strategy.service.IJsonService;
import com.github.lesach.strategy.strategy.*;
import com.github.lesach.webapp.model.KeyValuePair;
import com.github.lesach.webapp.provider.IChartProvider;
import com.github.lesach.webapp.provider.IStockageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/quote")
public class ApiController {

    @Autowired
    private IndicatorProviderInterface indicatorProvider;

    @Autowired
    private IJsonService json;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private IStockageService stockageService;

    // inject via application.properties
    @Autowired
    private DeGiroClientInterface client;

    @Autowired
    private IChartProvider chartProvider;

    @GetMapping(path = "/isconnected", produces = "application/json")
    public String isconnected() {
        String status = null;
        try {
            status = client.IsConnected() ? "Connected" : "Not connected";
        }
        catch (Exception e) {
            status = e.getMessage();
        }
        return "{ \"status\": \"" + status + "\"}";
    }

    //
    @PostMapping(path = "/addstrategy", consumes = "application/json", produces = "application/json")
    public String addstrategy(
            @RequestBody Map<String, String> body) throws JsonProcessingException {
        stockageService.addStrategy(body.get("strategy"));

        List<KeyValuePair> result = stockageService.findStrategies(body.get("strategy")).entrySet().stream()
                .map(e -> new KeyValuePair() {{
                    setKey(e.getKey().toString());
                    setValue(e.getValue().getName());
                }}).collect(Collectors.toList());
        return json.toJson(result);
    }

    //
    @PostMapping(path = "/addtrigger", consumes = "application/json", produces = "application/json")
    public String addtrigger(
            @RequestBody Map<String, String> body) throws IOException {
        DLog.info("Trigger received:" + json.toJson(body));

        TradingStrategy strategy = stockageService.getStrategy(body.get("strategyName"));
        if (strategy != null) {
            StrategyCore strategyCore = strategy.getStrategies().stream().findFirst().orElseGet(() -> {
                StrategyCore core = new StrategyCore();
                strategy.getStrategies().add(core);
                return core;
            });

            StrategyStep step = new StrategyStep();
            step.PeriodInstantType = EPeriodInstantType.getByValue(body.get("instantType"));
            int groupId = Integer.parseInt(body.get("group"));
            StrategyStepConditionGroup group = step.Groups.stream().filter(g -> g.Group == groupId).findFirst().orElseGet(() -> {
                StrategyStepConditionGroup g = new StrategyStepConditionGroup() {{ Group = groupId; }};
                step.Groups.add(g);
                return g;
            });
            group.booleanOperator = EBooleanOperator.getByValue(body.get("groupOperator"));
            StrategyStepCondition condition = json.fromJson(body.get("condition"), StrategyStepCondition.class);
            group.Conditions.add(condition);
            strategyCore.getSteps().add(step);
        }
        return json.toJson(strategy);
    }


    //
    @PostMapping(path = "/searchstrategies", consumes = "application/json", produces = "application/json")
    public String searchstrategies(
            @RequestBody Map<String, String> body) throws JsonProcessingException {
        List<KeyValuePair> result = stockageService.findStrategies(body.get("strategy")).entrySet().stream()
                .map(e -> new KeyValuePair() {{
                    setKey(e.getKey().toString());
                    setValue(e.getValue().getName());
                }}).collect(Collectors.toList());
        return json.toJson(result);
    }

    //
    @PostMapping(path = "/searchproducts", consumes = "application/json", produces = "application/json")
    public String searchproducts(
            @RequestBody Map<String, String> body) throws DeGiroException, JsonProcessingException {
        DProductSearch result =  client.SearchProducts(body.get("product"), DProductType.ALL, 10, 0);
        return json.toJson(result);
    }


    @PostMapping(path = "/gethistorical", consumes = "application/json", produces = "application/json")
    public String gethistorical(
            @RequestBody Map<String, String> body) throws IOException, DeGiroException {
        DProductDescription productDescription = json.fromJson(body.get("product"), DProductDescription.class);
        DPriceHistory history = client.GetPriceHistory(
                productDescription.getVwdIdentifierType(),
                productDescription.getVwdId(),
                LocalDateTime.parse(body.get("start").replace('T', ' '), dateTimeFormatter),
                LocalDateTime.parse(body.get("end").replace('T', ' '), dateTimeFormatter),
                body.get("resolution")
        );

        return json.toJson(history);
    }

    @PostMapping(path = "/chart", consumes = "application/json", produces = "application/json")
    public String getChart(
            @RequestBody Map<String, String> body) throws DeGiroException, IOException {
        DProductDescription productDescription = json.fromJson(body.get("product"), DProductDescription.class);
        DPriceHistory history = client.GetPriceHistory(
                productDescription.getVwdIdentifierType(),
                productDescription.getVwdId(),
                LocalDateTime.parse(body.get("start").replace('T', ' '), dateTimeFormatter),
                LocalDateTime.parse(body.get("end").replace('T', ' '), dateTimeFormatter),
                body.get("resolution")
        );

        return json.toJson(chartProvider.createChart(history));
    }


    @PostMapping(path = "/indicator", consumes = "application/json", produces = "application/json")
    public String getIndicator(
            @RequestBody Map<String, String> body) throws DeGiroException, IOException {
        List<BigDecimal> chart = json.fromJson(body.get("chart"), new TypeReference<List<BigDecimal>>(){});
        Indicator indicator = json.fromJson(body.get("indicator"), Indicator.class);
        List<BigDecimal> values  = indicatorProvider.ComputeIndicator(chart, indicator);
        return json.toJson(chartProvider.createLineDatasetFrom(values, indicator.getIndicatorType().getStrValue()));
    }
}
