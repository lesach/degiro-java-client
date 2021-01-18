package com.github.lesach.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lesach.strategy.EBooleanOperator;
import com.github.lesach.strategy.EPeriodInstantType;
import com.github.lesach.strategy.EStrategyType;
import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.service.IJsonService;
import com.github.lesach.strategy.strategy.EStrategyStepConditionParameterType;
import com.github.lesach.strategy.strategy.StrategyCore;
import com.github.lesach.strategy.strategy.StrategyStepCondition;
import com.github.lesach.webapp.provider.IStockageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WelcomeController {

    @Autowired
    protected IStockageService stockageService;

    @Autowired
    protected IJsonService jsonService;


    @GetMapping("/index")
    public String index(Model model) {
        Map<Indicator, String> indicators = Indicator.Indicators.stream().map(i -> {
                    try {
                        return new AbstractMap.SimpleEntry<>(
                                i,
                                jsonService.toJson(i)
                        );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("indicatorList", indicators);

        // Instant Type
        Map<EPeriodInstantType, String> periodInstantTypes = Arrays.stream(EPeriodInstantType.values()).map(i -> new AbstractMap.SimpleEntry<>(
                i,
                i.getStrValue()
        ))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("periodInstantTypeList", periodInstantTypes);

        // Boolean Operator List
        Map<EBooleanOperator, String> booleanOperators = Arrays.stream(EBooleanOperator.values()).map(i -> new AbstractMap.SimpleEntry<>(
                i,
                i.getStrValue()
        ))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("booleanOperatorList", booleanOperators);

        // Boolean Operator List
        Map<StrategyStepCondition, String> serieEventTypes =  StrategyStepCondition.StrategyStepConditions.stream().map(i -> {
            try {
                return new AbstractMap.SimpleEntry<>(
                        i,
                        jsonService.toJson(i)
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("serieEventTypeList", serieEventTypes);

        // Boolean Operator List
        Map<EStrategyStepConditionParameterType, String> strategyStepConditionParameterTypes = Arrays.stream(EStrategyStepConditionParameterType.values()).map(i -> new AbstractMap.SimpleEntry<>(
                i,
                i.getStrValue()
        ))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("strategyStepConditionParameterTypeList", strategyStepConditionParameterTypes);

        // Strategy Resolution Types
        Map<ETimeSerieResolutionType, String> strategyResolutions = Arrays.stream(ETimeSerieResolutionType.values()).map(i -> new AbstractMap.SimpleEntry<>(
                i,
                i.getStrValue()
        ))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("strategyResolutionList", strategyResolutions);

        // Strategy Resolution Types
        Map<EStrategyType, String> strategyTypes = Arrays.stream(EStrategyType.values()).map(i -> new AbstractMap.SimpleEntry<>(
                i,
                i.getStrValue()
        ))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        model.addAttribute("strategyTypeList", strategyTypes);

        model.addAttribute("strategyList", getStrategiesList());

        return "index";
    }

    @GetMapping("/test")
    public String test(Model model) {

        model.addAttribute("strategyList", getStrategiesList());

        return "scenariotester";
    }

    private Map<StrategyCore, String> getStrategiesList() {
        return stockageService.getStrategy().getStrategies().stream().map(i -> {
            try {
                return new AbstractMap.SimpleEntry<>(
                        i,
                        jsonService.toJson(i)
                );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
    }

}