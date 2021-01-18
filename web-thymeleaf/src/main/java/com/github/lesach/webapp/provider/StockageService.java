package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.engine.StrategyEngineInterface;
import com.github.lesach.strategy.service.IJsonService;
import com.github.lesach.strategy.strategy.StrategyCore;
import com.github.lesach.strategy.strategy.TradingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;

@Service
public class StockageService implements IStockageService{

    @Value("${strategy.path}")
    protected String strategyPath;

    @Autowired
    protected StrategyEngineInterface engine;

    @Autowired
    protected IJsonService json;

    @PostConstruct
    private void postConstruct() throws IOException {
        if (!StringUtils.isEmpty(strategyPath)) {
            File file = new File(strategyPath);
            if (file.exists()) {
                String jsonText = String.join("", readFileInList(strategyPath));
                engine.setStrategy(json.fromJson(jsonText, TradingStrategy.class));
            }
        }
        if (engine.getStrategy() == null) {
            engine.setStrategy(new TradingStrategy() {{
                setName("Strategies");
            }});
        }
    }

    @Override
    public TradingStrategy getStrategy() {
        return engine.getStrategy();
    }

    @Override
    public StrategyCore getStrategyCore(String strategyName) {
        return getStrategy().getStrategies()
                .stream().filter(s -> s.getName().equals(strategyName))
                .findFirst().orElse(null);
    }

    public static List<String> readFileInList(String fileName)
    {
        List<String> lines = Collections.emptyList();
        try
        {
            lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            // do something
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public void saveStrategy() throws IOException {
        File file = new File(strategyPath);
        if (file.exists())
            Files.copy(Paths.get(strategyPath), Paths.get(strategyPath + ".bkp"));
        BufferedWriter writer = new BufferedWriter(new FileWriter(strategyPath));
        writer.write(json.toJson(engine.getStrategy()));
        writer.close();
    }

    @PreDestroy
    private void preDestroy() throws IOException {
        saveStrategy();
    }
}
