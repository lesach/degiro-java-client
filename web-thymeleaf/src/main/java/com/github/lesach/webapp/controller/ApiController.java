package com.github.lesach.webapp.controller;

import be.ceau.chart.LineChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.options.annotation.Annotation;
import be.ceau.chart.options.annotation.AnnotationElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.lesach.client.DPriceHistory;
import com.github.lesach.client.DProductDescription;
import com.github.lesach.client.DProductSearch;
import com.github.lesach.client.DProductType;
import com.github.lesach.client.exceptions.DeGiroException;
import com.github.lesach.client.log.DLog;
import com.github.lesach.strategy.*;
import com.github.lesach.strategy.engine.StrategyEngineInterface;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.serie.IndicatorProviderInterface;
import com.github.lesach.strategy.service.IJsonService;
import com.github.lesach.strategy.strategy.*;
import com.github.lesach.webapp.model.KeyValuePair;
import com.github.lesach.webapp.provider.IChartProvider;
import com.github.lesach.webapp.provider.IStockageService;
import com.github.lesach.webapp.provider.ITreeviewProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/quote")
public class ApiController {
    @Autowired
    protected ITreeviewProvider treeviewProvider;

    @Autowired
    protected IndicatorProviderInterface indicatorProvider;

    @Autowired
    protected IJsonService json;

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    protected IStockageService stockageService;

    @Autowired
    protected StrategyEngineInterface engine;

    // inject via application.properties
    @Autowired
    protected DeGiroClientInterface client;

    @Autowired
    protected IChartProvider chartProvider;

    @GetMapping(path = "/isconnected", produces = "application/json")
    public String isconnected() {
        String status;
        try {
            status = client.IsConnected() ? "Connected" : "Not connected";
        }
        catch (Exception e) {
            status = e.getMessage();
        }
        return "{ \"status\": \"" + status + "\"}";
    }

    //
    @PostMapping(path = "/setresolution", consumes = "application/json", produces = "application/json")
    public String setresolution(
            @RequestBody Map<String, String> body) throws JsonProcessingException {
        DLog.info("Resolution received:" + json.toJson(body));
        TradingStrategy strategy = stockageService.getStrategy();
        strategy.setResolution(ETimeSerieResolutionType.getByValue(body.get("resolution")));
        return "{ \"message\":" + "\"OK\"" +"}";
    }

    @PostMapping(path = "/setstrategyproduct", consumes = "application/json", produces = "application/json")
    public String setstrategyproduct(
            @RequestBody Map<String, String> body) throws IOException {
        DLog.info("Product received.");
        DProductDescription productDescription = json.fromJson(body.get("product"), DProductDescription.class);
        StrategyCore core = stockageService.getStrategyCore(body.get("strategyName"));
        core.setProduct(productDescription);
        return "{ \"message\":" + "\"OK\"" +"}";
    }

    //
    @PostMapping(path = "/getstrategytreeview", consumes = "application/json", produces = "application/json")
    public String getstrategytreeview(
            @RequestBody Map<String, String> body) throws JsonProcessingException {
        DLog.info("Trigger received:" + json.toJson(body));
        TradingStrategy strategy = stockageService.getStrategy();
        return json.toJson(treeviewProvider.toTreeview(strategy));
    }

    //
    @PostMapping(path = "/addtrigger", consumes = "application/json", produces = "application/json")
    public String addtrigger(
            @RequestBody Map<String, String> body) throws IOException {
        DLog.info("Trigger received:" + json.toJson(body));

        TradingStrategy strategy = stockageService.getStrategy();
        if (strategy != null) {
            StrategyCore strategyCore = strategy.getStrategies().stream()
                    .filter(s -> s.getName().equals(body.get("strategyName")))
                    .findFirst().orElseGet(() -> {
                StrategyCore core = new StrategyCore() {{
                    setName(body.get("strategyName"));
                }};
                strategy.getStrategies().add(core);
                return core;
            });

            StrategyStep step = new StrategyStep();
            step.setPeriodInstantType(EPeriodInstantType.getByValue(body.get("instantType")));
            int groupId = Integer.parseInt(body.get("group"));
            StrategyStepConditionGroup group = step.getGroups().stream().filter(g -> g.getGroup() == groupId).findFirst().orElseGet(() -> {
                StrategyStepConditionGroup g = new StrategyStepConditionGroup() {{ setGroup(groupId); }};
                step.getGroups().add(g);
                return g;
            });
            group.setBooleanOperator(EBooleanOperator.getByValue(body.get("groupOperator")));
            StrategyStepCondition condition = json.fromJson(body.get("condition"), StrategyStepCondition.class);
            group.getConditions().add(condition);
            strategyCore.getSteps().add(step);
        }
        stockageService.saveStrategy();
        return json.toJson(treeviewProvider.toTreeview(strategy));
    }


    //
    @PostMapping(path = "/searchstrategies", consumes = "application/json", produces = "application/json")
    public String searchstrategies(
            @RequestBody Map<String, String> body) throws JsonProcessingException {
        List<KeyValuePair> result = stockageService.getStrategy().getStrategies()
                .stream().filter(c -> c.getName().equals(body.get("strategy")))
                .map(e -> new KeyValuePair() {{
                    setKey(e.getName());
                    try {
                        setValue(json.toJson(e));
                    } catch (JsonProcessingException jsonProcessingException) {
                        jsonProcessingException.printStackTrace();
                    }
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

    @PostMapping(path = "/teststrategy", consumes = "application/json", produces = "application/json")
    public String teststrategy(
            @RequestBody Map<String, String> body) throws IOException, DeGiroException, CloneNotSupportedException, InterruptedException {
        StrategyCore core = stockageService.getStrategyCore(body.get("strategyName"));
        LocalDateTime start = LocalDateTime.parse(body.get("start").replace('T', ' '), dateTimeFormatter);
        LocalDateTime end = LocalDateTime.parse(body.get("end").replace('T', ' '), dateTimeFormatter);

        DPriceHistory history = client.GetPriceHistory(
                core.getProduct().getVwdIdentifierType(),
                core.getProduct().getVwdId(),
                start,
                end,
                stockageService.getStrategy().getResolution().toString()
        );

        // Start the simulation
        engine.Simulate(start, end);

        // Create the data for the chart
        LineChart chart = new LineChart();
        List<? extends MeasureModel> values = engine.FindReference(core);
        values.sort(Comparator.comparing(MeasureModel::getDateTime));
        DateTimeFormatter dateTimeFormatter = DPriceHistory.getDateTimeFormatter(engine.getStrategy().getResolution().toString());
        LineData data = new LineData()
                .addDataset(chartProvider.createLineDataset(values, core.getProduct().getName() + " (" + core.getProduct().getIsin() + ")"))
                .setLabels(values.stream().map(v -> v.getDateTime().format(dateTimeFormatter)).collect(Collectors.toList()));
        chart.setData(data);

        // Ajoute les sections
        BigDecimal min = values.stream().map(MeasureModel::getValue).min(BigDecimal::compareTo).orElse(new BigDecimal(0));
        BigDecimal max = values.stream().map(MeasureModel::getValue).max(BigDecimal::compareTo).orElse(new BigDecimal(0));
        Annotation annotation = new Annotation();
        annotation.setDrawTime("afterDraw");
        /*
        annotations: [{
            type: 'box',
                    xScaleID: 'x-axis-1',
                    yScaleID: 'y-axis-1',
                    xMin: -120,
                    xMax: 20,
                    yMin: -120,
                    yMax: 20,
                    backgroundColor: 'rgba(101, 33, 171, 0.5)',
                    borderColor: 'rgb(101, 33, 171)',
                    borderWidth: 1,
                    onDblclick: function(e) {
                console.log('Box', e.type, this);
            }
        }]
        */
        core.getPeriods().forEach(p -> {
            AnnotationElement annotationElement = new AnnotationElement() {{
                setBackgroundColor(new Color(101, 33, 171, 0.5));
                setBorderColor(new Color(101, 33, 171));
                setBorderWidth("1");
                setXMin(p.getOhlc().getDate().format(dateTimeFormatter));
                setXMax(p.getEnd().format(dateTimeFormatter));
                setYMin(min.toString());
                setYMax(max.toString());
            }};
            annotation.getAnnotations().add(annotationElement);
        });
        chart.getOptions().setPlugins(annotation);
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
            @RequestBody Map<String, String> body) throws IOException {
        List<BigDecimal> chart = json.fromJson(body.get("chart"), new TypeReference<List<BigDecimal>>(){});
        Indicator indicator = json.fromJson(body.get("indicator"), Indicator.class);
        List<BigDecimal> values  = indicatorProvider.ComputeIndicator(chart, indicator);
        return json.toJson(chartProvider.createLineDatasetFrom(values, indicator.getIndicatorType().getStrValue()));
    }
}
