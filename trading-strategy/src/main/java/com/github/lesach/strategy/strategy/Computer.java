
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
import java.util.stream.Collectors;

public class Computer
{
    /// <summary>
    /// Compute periods matching events
    /// </summary>
    public void ComputeStatus(StrategyCore strategy,
                              List<LocalDateTime> reference,
                              List<SerieBase> ohlcSeries)
    {
        LocalDateTime tmp = LocalDateTime.MIN;
        if (strategy.Statuses.size() > 0) {
            Comparator<SerieEventStatus> cmp = Comparator.comparing(SerieEventStatus::getDate);
            tmp = strategy.Statuses.stream().max(cmp).get().getDate();
        }
        LocalDateTime statusMaxDate = tmp;
        // Event list
        Map<StrategyStep, List<SerieEventStatus>> seriesEventsStatus = new HashMap<StrategyStep, List<SerieEventStatus>>();
        for (StrategyStep serieEvent : strategy.Steps)
        {
            // Group
            Map<StrategyStepConditionGroup, List<SerieEventStatus>> seriesGroupsStatus = new HashMap<StrategyStepConditionGroup, List<SerieEventStatus>>();
            for (StrategyStepConditionGroup serieEventGroup : serieEvent.Groups)
            {
                // Items
                Map<StrategyStepCondition, List<SerieEventStatus>> serieEventItemsStatus = new HashMap<StrategyStepCondition, List<SerieEventStatus>>();
                for (StrategyStepCondition item : serieEventGroup.Conditions)
                {
                    List<SerieEventStatus> serieEventItemStatus = reference.stream()
                            .map(o -> new SerieEventStatus() {{ setDate(o); Verified = false; }}).collect(Collectors.toList());
                    List<SerieEventStatus> serieEventItemStatusToAdd = new ArrayList<SerieEventStatus>();
                    BigDecimal previous = BigDecimal.valueOf(-1);

                    for (SerieEventStatus status : serieEventItemStatus)
                    {
                        MeasureModel value;
                        switch (item.EventType)
                        {
                            case DerivateSignChangeToDecrease:
                                value = ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie", SerieBase.class).Key))
                                        .findFirst().orElseThrow()
                                        .Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElseThrow();
                                if (previous.compareTo(BigDecimal.ZERO) > 0 && value != null)
                                    status.Verified = value.getValue().compareTo(previous) <= 0;
                                if (value != null)
                                    previous = value.getValue();
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;
                            case DerivateSignChangeToIncrease:
                                value = ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie", SerieBase.class).Key))
                                        .findFirst().orElseThrow()
                                        .Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElseThrow();
                                if (previous.compareTo(BigDecimal.ZERO) > 0 && value != null)
                                    status.Verified = value.getValue().compareTo(previous) >= 0;
                                if (value != null)
                                    previous = value.getValue();
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;

                            case CrossDown:
                            case CrossUp:
                                MeasureModel valueA = ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie A", SerieBase.class).Key))
                                        .findFirst().orElseThrow()
                                        .Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElseThrow();
                                MeasureModel valueB = ohlcSeries.stream()
                                        .filter(i -> i.Key.equals(item.GetParameterValue("Serie B", SerieBase.class).Key))
                                        .findFirst().orElseThrow()
                                        .Values.stream().filter(o -> o.getDateTime() == status.getDate()).findFirst().orElseThrow();
                                if (valueA != null && valueB != null)
                                {
                                    if (item.EventType == ESerieEventType.CrossDown)
                                        status.Verified = valueA.getValue().compareTo(valueB.getValue()) <= 0;
                                    else if (item.EventType == ESerieEventType.CrossUp)
                                        status.Verified = valueA.getValue().compareTo(valueB.getValue()) >= 0;
                                }
                                else
                                    previous = BigDecimal.valueOf(-1);
                                break;
                            case TimeAfter:
                            case TimeBefore:
                                LocalTime time = LocalTime.parse(item.GetParameterValue("Time", String.class), DUtils.HM_FORMAT);
                                if (item.EventType == ESerieEventType.TimeAfter)
                                    status.Verified = time.getSecond() >= status.getDate().getSecond();
                                if (item.EventType == ESerieEventType.TimeBefore)
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
                                Operator = i.getKey().BooleanOperator;
                                Value = i.getValue().stream().filter(o -> o.getDate() == groupStatus.getDate()).findFirst().orElseThrow().Verified;
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
                            Value = i.getValue().stream().filter(o -> o.getDate().isEqual(eventStatus.getDate())).findFirst().orElseThrow().Verified;
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
            boolean value = false;
            if (startStopevents.size() > 0)
                value = startStopevents.stream().map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).orElseThrow();
            // for Stop the value is inverted because
            if (periodInstantType == EPeriodInstantType.Stop)
                value = !value;

            List<SerieEventStatus> continueEvents = seriesEventsStatus.entrySet().stream()
                    .filter(s -> s.getKey().PeriodInstantType == EPeriodInstantType.Continue)
                    .map(s -> s.getValue().stream().filter(e -> e.getDate().isEqual(o)))
                    .flatMap(s -> s)
                    .collect(Collectors.toList());
            if (continueEvents.size() > 0)
                value = (value || (startStopevents.size() == 0))
                    && continueEvents.stream().map(s -> s.Verified).reduce((s1, s2) -> s1 && s2).orElseThrow();

            boolean finalValue = value;
            strategy.Statuses.add(new SerieEventStatus() {{ setDate(o); Verified = finalValue; }});
        }
    }

    /// <summary>
    /// Compute period based on
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="status"></param>
    /// <returns></returns>
    public void ComputePeriods(StrategyCore strategy, List<MeasureModel> reference) {
        if (strategy.Statuses.size() > 0)
        {
            // Check if we are : a period
            Period currentPeriod = strategy.Periods.stream()
                    .filter(p -> p.End.isAfter(strategy.LastPeriodComputation) || p.End.isEqual(strategy.LastPeriodComputation))
                    .findFirst().orElseThrow();
            EPeriodInstantType periodInstantType = EPeriodInstantType.Stop;
            if (currentPeriod == null)
            {
                currentPeriod = new Period() {{ ohlc = null; End = LocalDateTime.MIN; }};
                periodInstantType = EPeriodInstantType.Start;
            }
            LocalDateTime maxDate = strategy.Statuses.stream().map(o -> o.getDate()).max(LocalDateTime::compareTo).orElseThrow();
            for (MeasureModel o : reference.stream().filter(r -> r.getDateTime().isAfter(strategy.LastPeriodComputation)).collect(Collectors.toList()))
            {
                SerieEventStatus s = strategy.Statuses.stream().filter(l -> l.getDate() == o.getDateTime()).findFirst().orElseThrow();
                if (s.Verified)
                {
                    MeasureModel lastOhlc = reference.stream().filter(l -> l.getDateTime() == o.getDateTime()).findFirst().orElseThrow();
                    if (periodInstantType == EPeriodInstantType.Stop)
                    {
                        // Continuing
                        currentPeriod.End = lastOhlc.getDateTime();
                        currentPeriod.ohlc.Close = lastOhlc.getValue();
                        currentPeriod.ohlc.Low = currentPeriod.ohlc.Low.min(lastOhlc.getValue());
                        currentPeriod.ohlc.High = currentPeriod.ohlc.High.max(lastOhlc.getValue());
                    }
                    else
                    {
                        // Starting
                        periodInstantType = EPeriodInstantType.Stop;
                        currentPeriod.ohlc = new Ohlc();
                        currentPeriod.ohlc.Date = lastOhlc.getDateTime();
                        currentPeriod.ohlc.Open = lastOhlc.getValue();
                        currentPeriod.ohlc.Close = lastOhlc.getValue();
                        currentPeriod.ohlc.Low = lastOhlc.getValue();
                        currentPeriod.ohlc.High = lastOhlc.getValue();
                        currentPeriod.End = lastOhlc.getDateTime();
                        strategy.Periods.add(currentPeriod);
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
            strategy.LastPeriodComputation = maxDate;
        }
    }

    /// <summary>
    /// Compute Statistics over periods
    /// </summary>
    /// <param name="strategy"></param>
    /// <param name="periods"></param>
    /// <returns></returns>
    public List<ComputationStatistic> ComputeStatistics(EStrategyType strategy,
        List<Period> periods,
        BigDecimal computationMargin)
    {
        List<ComputationStatistic> result = new ArrayList<ComputationStatistic>();
        result.add(new ComputationStatistic() {{ Name = "count"; Value = BigDecimal.valueOf(periods.size()); }});
        if (periods.size() > 0)
        {
            result.add(new ComputationStatistic() {{ Name = "Average duration (mn)"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> ChronoUnit.SECONDS.between(p.ohlc.Date, p.End)).average().orElse(0d)); }});
            result.add(new ComputationStatistic() {{ Name = "Cumulative duration (mn)"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> ChronoUnit.SECONDS.between(p.ohlc.Date, p.End)).sum()); }});
            result.add(new ComputationStatistic() {{ Name = "Average delta"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> p.ohlc.Close.subtract(p.ohlc.Open).doubleValue()).average().orElse(0d)); }});
            result.add(new ComputationStatistic() {{ Name = "Cumulative delta"; Value = BigDecimal.valueOf(periods.stream().mapToDouble(p -> p.ohlc.Close.subtract(p.ohlc.Open).doubleValue()).sum()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta > " + computationMargin; Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.Close.subtract(p.ohlc.Open).compareTo(computationMargin) > 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta < " + (computationMargin.multiply(BigDecimal.valueOf(-1))); Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.Close.subtract(p.ohlc.Open).compareTo(computationMargin.multiply(BigDecimal.valueOf(-1))) < 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta max > " + computationMargin; Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.High.subtract(p.ohlc.Open).compareTo(computationMargin) > 0).count()); }});
            result.add(new ComputationStatistic() {{ Name = "Number of delta min < " + (computationMargin.multiply(BigDecimal.valueOf(-1))); Value = BigDecimal.valueOf(periods.stream().filter(p -> p.ohlc.Low.subtract(p.ohlc.Open).compareTo(computationMargin.multiply(BigDecimal.valueOf(-1))) < 0).count()); }});
        }
        return result;
    }
}