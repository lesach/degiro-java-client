package com.github.lesach.webapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
import com.github.lesach.client.DPriceHistory;
import com.github.lesach.client.DProductSearch;
import com.github.lesach.client.DProductType;
import com.github.lesach.strategy.DeGiroClientInterface;
import com.github.lesach.client.engine.json.DConvert;
import com.github.lesach.client.exceptions.DeGiroException;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class WelcomeController {

    /*
    ObjectMapper json = DConvert.getObjectMapper();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    // inject via application.properties
    @Autowired
    private DeGiroClientInterface client;

    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("tasks", tasks);
        return "welcome"; //view
    }

    // /hello?name=kotlin
    @PostMapping(path = "/quote/searchproducts", consumes = "application/json", produces = "application/json")
    public String searchproducts(
            @RequestBody Map<String, String> body) throws DeGiroException, JsonProcessingException {
        DProductSearch result =  client.SearchProducts(body.get("name"), DProductType.ALL, 10, 0);
        return json.writeValueAsString(result);
    }


    @PostMapping(path = "/quote/gethistorical", consumes = "application/json", produces = "application/json")
    public String gethistorical(
            @RequestBody Map<String, String> body) throws DeGiroException, JsonProcessingException {

        DPriceHistory history = client.GetPriceHistory(
                body.get("vwdIdentifierType"),
                body.get("vwdId"),
                LocalDateTime.parse(body.get("start"), dateTimeFormatter),
                LocalDateTime.parse(body.get("end"), dateTimeFormatter),
                body.get("resolution")
        );

        return json.writeValueAsString(history);
    }
*/
}