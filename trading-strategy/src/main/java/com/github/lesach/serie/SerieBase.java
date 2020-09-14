package com.github.lesach.serie;

import com.github.lesach.ETimeSerieResolutionType;
import com.github.lesach.MeasureModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SerieBase
{
    public SerieKey Key;
    public List<? extends MeasureModel> Values;

    public SerieBase(SerieKey key, List<? extends MeasureModel> values)
    {
        this.Key = key;
        this.Values = values;
    }

    /// <summary>
    /// Return a String representign the Object
    /// </summary>
    @Override
    public String toString() {
        return Key.toString();
    }

    /// <summary>
    /// Return a scaled serie
    /// </summary>
    /// <param name="serie"></param>
    /// <returns></returns>
    public List<MeasureModel> FormatSerie(ETimeSerieResolutionType resolution)
    {
        // Compute result only on the "Last" serie
        return new ArrayList<>(this.Values
                .stream()
                .collect(
                        Collectors.toMap(g -> resolution.getRoundedDateTime(g.getDateTime()), Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(MeasureModel::getValue)))
                ).values());
    }
}
