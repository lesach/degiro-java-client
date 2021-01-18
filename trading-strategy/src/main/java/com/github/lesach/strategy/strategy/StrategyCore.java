package com.github.lesach.strategy.strategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lesach.client.*;
import com.github.lesach.strategy.EStrategyType;
import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.Period;
import com.github.lesach.strategy.SerieEventStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StrategyCore
{
    private String name;
    private DProductDescription Product;
    private EStrategyType StrategyType;
    private List<StrategyStep> Steps = new ArrayList<>();

    @JsonIgnore
    private LocalDateTime LastPeriodComputation = LocalDateTime.MIN;

    @JsonIgnore
    private List<Period> Periods = new ArrayList<>();

    @JsonIgnore
    private List<SerieEventStatus> Statuses = new ArrayList<>();

    @JsonIgnore
    private List<ComputationStatistic> Statistics = new ArrayList<>();

    @JsonIgnore
    private final Computer computer = new Computer();

    @JsonIgnore
    private LocalDateTime StartDate = LocalDateTime.MIN;

    /// <summary>
    /// Compute Result
    /// </summary>
    /// <param name="series"></param>
    public void ComputeStatistics(BigDecimal computationMargin)
    {
        Statistics.clear();
        List<ComputationStatistic> statistics = computer.ComputeStatistics(Periods, computationMargin);
        Statistics.addAll(statistics);
    }

    @Override
    public String toString() {
        return this.getName() + ((getStrategyType() == null ) ? Strings.EMPTY : " - " + getStrategyType().toString());
    }
}
