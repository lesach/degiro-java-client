package com.github.lesach.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lesach.strategy.EBooleanOperator;
import com.github.lesach.strategy.EPeriodInstantType;
import com.github.lesach.strategy.ESerieEventType;
import com.github.lesach.strategy.serie.Indicator;
import com.github.lesach.strategy.service.IJsonService;
import com.github.lesach.strategy.strategy.EStrategyStepConditionParameterType;
import com.github.lesach.strategy.strategy.StrategyStepCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.lesach.strategy.strategy.StrategyStepCondition.StrategyStepConditions;

@Controller
public class WelcomeController {

    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    @Autowired
    private IJsonService jsonService;

    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

        return "welcome"; //view
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

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

        return "index";
    }

    // /hello?name=kotlin
    @GetMapping("/hello")
    public String mainWithParam(
            @RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {

        model.addAttribute("message", name);

        return "welcome"; //view
    }

}