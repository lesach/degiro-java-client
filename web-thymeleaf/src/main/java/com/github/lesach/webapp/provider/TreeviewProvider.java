package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.strategy.TradingStrategy;
import com.github.lesach.webapp.model.BSTreeviewNode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeviewProvider implements ITreeviewProvider {

    public List<BSTreeviewNode> toTreeview(TradingStrategy strategy) {
        List<BSTreeviewNode> result = new ArrayList<>();
        // Add root node
        BSTreeviewNode rootNode = new BSTreeviewNode() {{
            setText(strategy.toString());
        }};
        result.add(rootNode);
        // Strategy names
        rootNode.getNodes().addAll(
            strategy.getStrategies().stream().map(c->
                new BSTreeviewNode() {{
                    setText(c.toString());
                    getNodes().addAll(
                        c.getSteps().stream().map(s ->
                            new BSTreeviewNode() {{
                            setText(s.toString());
                            getNodes().addAll(
                                s.getGroups().stream().map(g ->
                                    new BSTreeviewNode() {{
                                        setText(g.toString());
                                        getNodes().addAll(
                                            g.getConditions().stream().map(d ->
                                                new BSTreeviewNode() {{
                                                    setText(d.toString());
                                                }}
                                            ).collect(Collectors.toList())
                                        );
                                    }}
                                ).collect(Collectors.toList())
                            );
                        }}
                    ).collect(Collectors.toList()));
                }}
            ).collect(Collectors.toList())
        );
        return result;
    }
}
