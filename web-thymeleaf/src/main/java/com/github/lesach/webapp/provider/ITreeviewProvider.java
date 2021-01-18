package com.github.lesach.webapp.provider;

import com.github.lesach.strategy.strategy.TradingStrategy;
import com.github.lesach.webapp.model.BSTreeviewNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface ITreeviewProvider {
    public List<BSTreeviewNode> toTreeview(TradingStrategy strategy);
}
