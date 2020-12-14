
package com.github.lesach.strategy.strategy;

import com.github.lesach.indicator.Ohlc;
import com.github.lesach.strategy.*;
import com.github.lesach.strategy.serie.SerieBase;
import com.github.lesach.client.utils.DUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Computer
{
    private final Comparator<SerieEventStatus> serieEventStatusComparator = Comparator.comparing(SerieEventStatus::getDate);

    /// <summary>
    /// Compute periods matching events
    /// </summary>
    public void ComputeStatus(StrategyCore strategy,
                              List<LocalDateTime> reference,
                              List<SerieBase> ohlcSeries)
    {
        AtomicReference<LocalDateTime> tmp = new AtomicReference<LocalDateTime>() {{ set(LocalDateTime.MIN); }};
        strategy.getStatuses().stream().max(serieEventStatusComparator).ifPresent(d -> tmp.set(d.getDate()));
        LocalDateTime statusMaxDate = tmp.get();
        // Event list
        Map<StrategyStep, List<SerieEventStatus>> seriesEventsStatus = new HashMap<>();
        AtomicReference<MeasureModel> value = new AtomicReference<>();
        AtomicReference<MeasureModel> valueA = new AtomicReference<>();
        AtomicReference<MeasureModel> valueB = new AtomicReference<>();
        for (StrategyStep serieEvent : strategy.getSteps())
        {
            // Group
            Map<StrategyStepConditionGroup, List<SerieEventStatus>> seriesGroupsStatus = new HashMap<>();
            for (StrategyStepConditionGroup serieEventGroup : serieEvent.Groups)
            {
                // Items
                Map<StrategyStepCondition, List<SerieEventStatus>> serieEventItemsStatus = new HashMap<>();
                for (StrategyStepCondition item : serieEventGroup.Conditions)
                {
                    List<SerieEventStatus> serieEventItemStatus = reference.stream()
                            .map(o -> new SerieEventStatus() {{ setDate(o); Verified = false; }}).collect(Collectors.toList());
                    List<SerieEventStatus> serieEventItemStatusToAdd = new ArrayList<>();
                    BigDecimal previous = BigDecimal.valueOf(-1);
                    for (SerieEventStatus status : serieEventItemStatus)
                    {
                        switch (item.getEventType())
                        {
                            case DerivateSignChangeToDecrease:
                                ohlcSeries.stream()
                                    .filter(i -> i.Key.equals(item.GetParameterValue("Serie", SerieBase.class).Key))
                                    .findFirst().ifPresent( s ->
                                        value.set(s.Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElse(null))
                                    );
                                if (previous.compareTo(BigDecimal.ZERO) > 0 && value.get() != null)
                                    status.Verified = value.get().getValue().compareTo(previous) <= 0;
                                if (value.get() != null)
                                    previous = value.get().getValue();
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;
                            case DerivateSignChangeToIncrease:
                                ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie", SerieBase.class).Key))
                                        .findFirst().ifPresent( s ->
                                        value.set(s.Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElse(null))
                                );
                                if (previous.compareTo(BigDecimal.ZERO) > 0 && value.get() != null)
                                    status.Verified = value.get().getValue().compareTo(previous) >= 0;
                                if (value.get() != null)
                                    previous = value.get().getValue();
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;

                            case CrossDown:
                            case CrossUp:
                                ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie A", SerieBase.class).Key))
                                        .findFirst().ifPresent( s ->
                                        valueA.set(s.Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElse(null))
                                );
                                ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie B", SerieBase.class).Key))
                                        .findFirst().ifPresent( s ->
                                        valueB.set(s.Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElse(null))
                                );
                                if (valueA.get() != null && valueB.get() != null)
                                {
                                    if (item.getEventType() == ESerieEventType.CrossDown)
                                        status.Verified = valueA.get().getValue().compareTo(valueB.get().getValue()) <= 0;
                                    else if (item.getEventType() == ESerieEventType.CrossUp)
                                        status.Verified = valueA.get().getValue().compareTo(valueB.get().getValue()) >= 0;
                                }
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;
                            case TimeAfter:
                            case TimeBefore:
                                LocalTime time = LocalTime.parse(item.GetParameterValue("Time", String.class), DUtils.HM_FORMAT);
                                if (item.getEventType() == ESerieEventType.TimeAfter)
                                    status.Verified = time.getSecond() >= status.getDate().getSecond();
                                if (item.getEventType() == ESerieEventType.TimeBefore)
                                    status.Verified = time.getSecond() <= status.getDate().getSecond();
                                break;
                            case TimeBetween:
                                LocalTime timeA = LocalTime.parse(item.GetParameterValue("Time A", String.class));
                                LocalTime timeB = LocalTime.parse(item.GetParameterValue("Time B", String.class));
                                status.Verified = timeA.getSecond() <= status.getDate().getSecond()
                                    && timeB.getSecond() >= status.getDate().getSecond();
                                break;
                        }
                        if (status.getDate().isAfter(statusMaxDate))
                            serieEventItemStatusToAdd.add(status);
                    }
                    serieEventItemsStatus.put(item, serieEventItemStatusToAdd);
                }

                // Merge Items to group
                List<SerieEventStatus> serieEventGroupStatus = reference.stream().filter(r -> r.isAfter(statusMaxDate))
                        .map(o -> new SerieEventStatus() {{ setDate(o); Verified = false; }})
                        .collect(Collectors.toList());
                for (SerieEventStatus groupStatus : serieEventGroupStatus)
                {
                    List<BooleanOperand> operation = serieEventItemsStatus.entrySet().stream()
                            .map(i -> new BooleanOperand() {{
                                Operator = i.getKey().getBooleanOperator();
                                Value = i.getValue().stream().filter(o -> o.getDate() == groupStatus.getDate()).findFirst().get().Verified;
                            }})
                            .collect(Collectors.toList());
                    boolean first = true;
                    boolean result = false;
                    for (BooleanOperand o : operation)
                    {
                        if (first)
                        {
                            result = o.Value;
                            first = false;
                        }
                        else
                        {
                            switch (o.Operator)
                            {
                                case AND:
                                    result = result && o.Value;
                                    break;
                                case OR:
                                    result = result || o.Value;
                                    break;
                                case XOR:
                                    result = result != o.Value;
                                    break;
                                default: break;
                            }
                        }
                    }
                    groupStatus.Verified = result;
                }
                seriesGroupsStatus.put(serieEventGroup, serieEventGroupStatus);
            }

            // Merge Groups to event
            List<SerieEventStatus> serieEventStatus = reference.stream().filter(r -> r.isAfter(statusMaxDate))
                .map(o -> new SerieEventStatus() {{ setDate(o); Verified = false; }}).collect(Collectors.toList());
            for (SerieEventStatus eventStatus : serieEventStatus)
            {
                List<BooleanOperand> operation = seriesGroupsStatus.entrySet().stream()
                        .map(i -> new BooleanOperand() {{
                            Operator = i.getKey().booleanOperator;
                            Value = i.getValue().stream().filter(o -> o.getDate().isEqual(eventStatus.getDate())).findFirst().get().Verified;
                        }}).collect(Collectors.toList());
                boolean first = true;
                boolean result = false;
                for (BooleanOperand o : operation)
                {
                    if (first)
                    {
                        result = o.Value;
                        first = false;
                    }
                    else
                    {
                        switch (o.Operator)
                        {
                            case AND:
                                result = result && o.Value;
                                break;
                            case OR:
                                result = result || o.Value;
                                break;
                            case XOR:
                                result = result != o.Value;
                                break;
                            default: break;
                        }
                    }
                }
                eventStatus.Verified = result;
            }
            seriesEventsStatus.put(serieEvent, serieEventStatus);
        }

        // Compute periods
        EPeriodInstantType periodInstantType = EPeriodInstantType.Start;
        for (LocalDateTime o : reference.stream().filter(r -> r.isAfter(statusMaxDate)).collect(Collectors.toList()))
        {
            List<SerieEventStatus> startStopevents = seriesEventsStatus.entrySet().stream()
                    .filter(s -> s.getKey().PeriodInstantType == periodInstantType)
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            boolean bValue = false;
            if (startStopevents.size() > 0)
                bValue = startStopevents.stream().map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).get();
            // for Stop the value is inverted because
            //if (periodInstantType == EPeriodInstantType.Stop)
            //    bValue = !bValue;

            List<SerieEventStatus> continueEvents = seriesEventsStatus.entrySet().stream()
                    .filter(s -> s.getKey().PeriodInstantType == EPeriodInstantType.Continue)
                    .flatMap(s -> s.getValue().stream().filter(e -> e.getDate().isEqual(o)))
                    .collect(Collectors.toList());
            if (continueEvents.size() > 0)
                bValue = (bValue || (startStopevents.size() == 0))
                    && continueEvents.stream().map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).get();

            boolean finalValue = bValue;
            strategy.getStatuses().add(new SerieEventStatus() {{ setDate(o); Verified = finalValue; }});
        }
    }

    /// <summary>
    /// Compute period based on
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="status"></param>
    /// <returns></returns>
    public void ComputePeriods(StrategyCore strategy, List<MeasureModel> reference) {
        if (strategy.getStatuses().size() > 0)
        {
            // Check if we are : a period
            Period currentPeriod = strategy.getPeriods().stream()
                    .filter(p -> p.End.isAfter(strategy.getLastPeriodComputation()) || p.End.isEqual(strategy.getLastPeriodComputation()))
                    .findFirst().orElse(null);
            EPeriodInstantType periodInstantType = EPeriodInstantType.Stop;
            LocalDateTime maxDate = strategy.getStatuses().stream().map(SerieEventStatus::getDate).max(LocalDateTime::compareTo).orElse(null);
            for (MeasureModel o : reference.stream().filter(r -> r.getDateTime().isAfter(strategy.getLastPeriodComputation())).collect(Collectors.toList()))
            {
                SerieEventStatus s = strategy.getStatuses().stream().filter(l -> l.getDate() == o.getDateTime()).findFirst().orElse(null);
                assert s != null;
                if (s.Verified)
                {
                    MeasureModel lastOhlc = reference.stream().filter(l -> l.getDateTime() == o.getDateTime()).findFirst().orElse(null);
                    assert lastOhlc != null;
                    if (periodInstantType == EPeriodInstantType.Stop)
                    {
                        // Continuing
                        assert currentPeriod != null;
                        currentPeriod.End = lastOhlc.getDateTime();
                        currentPeriod.ohlc.setClose(lastOhlc.getValue());
                        currentPeriod.ohlc.setLow(currentPeriod.ohlc.getLow().min(lastOhlc.getValue()));
                        currentPeriod.ohlc.setHigh(currentPeriod.ohlc.getHigh().max(lastOhlc.getValue()));
                    }
                    else
                    {
                        // Starting
                        periodInstantType = EPeriodInstantType.Stop;
                        currentPeriod.ohlc = new Ohlc();
                        currentPeriod.ohlc.setDate(lastOhlc.getDateTime());
                        currentPeriod.ohlc.setOpen(lastOhlc.getValue());
                        currentPeriod.ohlc.setClose(lastOhlc.getValue());
                        currentPeriod.ohlc.setLow(lastOhlc.getValue());
                        currentPeriod.ohlc.setHigh(lastOhlc.getValue());
                        currentPeriod.End = lastOhlc.getDateTime();
                        strategy.getPeriods().add(currentPeriod);
                    }
                }
                else {
                    // Stopping
                    if (periodInstantType == EPeriodInstantType.Stop)
                    {
                        // Reset section
                        periodInstantType = EPeriodInstantType.Start;
                        currentPeriod = new Period() {{ ohlc = null; End = LocalDateTime.MIN; }};
                    }
                }
            }
            strategy.setLastPeriodComputation(maxDate);
        }
    }

    /// <summary>
    /// Compute Statistics over periods
    /// </summary>
    /// <param name="strategy"></param>
    /// <param name="periods"></param>
    /// <returns></returns>
    public List<ComputationStatistic> ComputeStatistics(List<Period> periods, BigDecimal computationMargin)
    {
        List<ComputationStatistic> result = new ArrayList<>();
        result.add(new ComputationStatistic() {{ Name = "count"; Value = BigDecimal.valueOf(periods.size()); }});
        if (periods.size() > 0)
        {
            result.add(new ComputationStatistic() {{ Name = "Average duration (mn)"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> ChronoUnit.SECONDS.between(p.ohlc.getDate(), p.End)).average().orElse(0d)); }});
            result.add(new ComputationStatistic() {{ Name = "Cumulative duration (mn)"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> ChronoUnit.SECONDS.between(p.ohlc.getDate(), p.End)).sum()); }});
            result.add(new ComputationStatistic() {{ Name = "Average delta"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> p.ohlc.getClose().subtract(p.ohlc.getOpen()).doubleValue()).average().orElse(0d)); }});
            result.add(new ComputationStatistic() {{ Name = "Cumulative delta"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> p.ohlc.getClose().subtract(p.ohlc.getOpen()).doubleValue()).sum()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta > " + computationMargin; Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.getClose().subtract(p.ohlc.getOpen()).compareTo(computationMargin) > 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta < " + (computationMargin.multiply(BigDecimal.valueOf(-1))); Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.getClose().subtract(p.ohlc.getOpen()).compareTo(computationMargin.multiply(BigDecimal.valueOf(-1))) < 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta max > " + computationMargin; Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.getHigh().subtract(p.ohlc.getOpen()).compareTo(computationMargin) > 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta min < " + (computationMargin.multiply(BigDecimal.valueOf(-1))); Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.getLow().subtract(p.ohlc.getOpen()).compareTo(computationMargin.multiply(BigDecimal.valueOf(-1))) < 0).count()); }});
        }
        return result;
    }
}