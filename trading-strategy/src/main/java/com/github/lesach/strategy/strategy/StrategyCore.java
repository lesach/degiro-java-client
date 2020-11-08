
package com.github.lesach.strategy.strategy;

import com.github.lesach.client.*;
import com.github.lesach.strategy.EStrategyType;
import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.Period;
import com.github.lesach.strategy.SerieEventStatus;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StrategyCore
{
    public String getName() {
        if (Product == null)
            return Strings.EMPTY;
        return Product.getName();
    }
    public DProductDescription Product;
    public EStrategyType StrategyType;
    public List<StrategyStep> Steps = new ArrayList<StrategyStep>();
    public ETimeSerieResolutionType Resolution;
    public LocalDateTime LastPeriodComputation = LocalDateTime.MIN;
    public List<Period> Periods = new ArrayList<Period>();
    public List<SerieEventStatus> Statuses = new ArrayList<SerieEventStatus>();
    public List<ComputationStatistic> Statistics = new ArrayList<ComputationStatistic>();
    private final Computer computer = new Computer();
    public LocalDateTime StartDate = LocalDateTime.MIN;

    /// <summary>
    /// Compute Result
    /// </summary>
    /// <param name="series"></param>
    public void ComputeStatistics(BigDecimal computationMargin)
    {
        Statistics.clear();
        List<ComputationStatistic> statistics = computer.ComputeStatistics(StrategyType, Periods, computationMargin);
        Statistics.addAll(statistics);
    }

}
