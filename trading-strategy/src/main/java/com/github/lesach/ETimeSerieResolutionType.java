package com.github.lesach;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import com.github.lesach.indicator.Ohlc;


public enum ETimeSerieResolutionType {
    NULL(-1, "NULL"),
    P1D(0, "day"),
    PT1H(1, "hour"),
    PT1M(2, "minute"),
    PT1S(3, "second");

    private final int value;
    private final String strValue;

    private ETimeSerieResolutionType(int value, String strValue) {
        this.value = value;
        this.strValue = strValue;
    }

    public int getValue() {
        return value;
    }

    public String getStrValue() {
        return strValue;
    }

    public static ETimeSerieResolutionType getByValue(int value) {
        ETimeSerieResolutionType type = null;
        int i = 0;
        ETimeSerieResolutionType[] values = ETimeSerieResolutionType.values();
        while (i < values.length && values[i].value != value) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public static ETimeSerieResolutionType getByValue(String value) {
        ETimeSerieResolutionType type = null;
        int i = 0;
        ETimeSerieResolutionType[] values = ETimeSerieResolutionType.values();
        while (i < values.length && !values[i].strValue.equals(value)) {
            i++;
        }
        if (i < values.length) {
            type = values[i];
        }

        return type;
    }

    public LocalTime getResolutionTimeSpan() {
        switch (this) {
            case P1D:
                return LocalTime.of(24, 0, 0);
            case PT1H:
                return LocalTime.of(1, 0, 0);
            case PT1M:
                return LocalTime.of(0, 1, 0);
            case PT1S:
                return LocalTime.of(0, 0, 1);
            default:
                return null;
        }
    }

    public LocalDateTime getRoundedDateTime(LocalDateTime date) {
        switch (this) {
            case P1D:
                return date.truncatedTo(ChronoUnit.DAYS);
            case PT1H:
                return date.truncatedTo(ChronoUnit.HOURS);
            case PT1M:
                return date.truncatedTo(ChronoUnit.MINUTES);
            case PT1S:
                return date.truncatedTo(ChronoUnit.SECONDS);
            default:
                return null;
        }
    }


    public String GetResolutionDateTime() {
        switch (this) {
            case PT1S:
                return "yyyy-MM-dd HH:mm:ss";
            case PT1M:
                return "yyyy-MM-dd HH:mm:00";
            case PT1H:
                return "yyyy-MM-dd HH:00:00";
            case P1D:
                return "yyyy-MM-dd 00:00:00";
            default:
                return "yyyy-MM-dd HH:mm:ss";
        }
    }

    public int GetResolutionStepMilliSeconds() {
        switch (this) {
            case PT1S:
                return 1000;
            case PT1M:
                return 60 * 1000;
            case PT1H:
                return 60 * 60 * 1000;
            case P1D:
                return 24 * 60 * 60 * 1000;
            default:
                return 1000;
        }
    }

    public LocalDateTime getRoundedDateTime(LocalDateTime dateTime, LocalTime timeSpan) {
        return dateTime.plusHours(timeSpan.getHour()).plusMinutes(timeSpan.getMinute())
                .plusSeconds(timeSpan.getSecond()).minusNanos(1);
    }

    public List<LocalDateTime> getDateTimeBetween(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime> steps;
        long duration;
        ChronoUnit tu;
        switch (this) {
            case PT1M:
                duration = ChronoUnit.MINUTES.between(start, end);
                tu = ChronoUnit.MINUTES;
                break;
            case PT1H:
                duration = ChronoUnit.HOURS.between(start, end);
                tu = ChronoUnit.HOURS;
                break;
            case P1D:
                duration = ChronoUnit.DAYS.between(start, end);
                tu = ChronoUnit.DAYS;
                break;
            default: //case ETimeSerieResolutionType.PT1S:
                duration = ChronoUnit.SECONDS.between(start, end);
                tu = ChronoUnit.SECONDS;
                break;
        }
        return LongStream.range(0, duration).boxed().collect(Collectors.toList())
                .stream()
                .map(l -> start.plus(l, tu))
                .collect(Collectors.toList());
    }

    public <T extends MeasureModel> List<MeasureModel> Normalize(List<T> values) {
        LocalTime ts = this.getResolutionTimeSpan();
        Map<LocalDateTime, List<MeasureModel>> m = values
                .stream()
                .collect(Collectors.groupingBy(v -> this.getRoundedDateTime(v.getDateTime(), ts)));

        return m.entrySet().stream().map(e -> new MeasureModel() {{
            setDateTime(e.getKey());
            setValue(e.getValue().stream().map(MeasureModel::getValue).reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(e.getValue().size()), MathContext.DECIMAL64));
        }}).collect(Collectors.toList());
    }

    public <T extends MeasureModel> List<Ohlc> NormalizeToOhlcList(List<T> values) {
        LocalTime ts = this.getResolutionTimeSpan();
        Map<LocalDateTime, List<MeasureModel>> m = values
                .stream()
                .collect(Collectors.groupingBy(v -> this.getRoundedDateTime(v.getDateTime(), ts)));

        return m.entrySet().stream()
                .map(e -> new Ohlc() {{
                    Date = e.getKey();
                    Close = e.getValue().stream().map(MeasureModel::getValue).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(BigDecimal.valueOf(e.getValue().size()), MathContext.DECIMAL64);
                    High = e.getValue().stream().map(MeasureModel::getValue).max(BigDecimal::compareTo).orElse(null);
                    Low = e.getValue().stream().map(MeasureModel::getValue).min(BigDecimal::compareTo).orElse(null);
                }})
                .collect(Collectors.toList());

    }
}
