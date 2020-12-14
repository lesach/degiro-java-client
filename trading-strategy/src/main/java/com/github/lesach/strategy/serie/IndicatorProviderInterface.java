package com.github.lesach.strategy.serie;

import com.github.lesach.indicator.EMA;
import com.github.lesach.indicator.Ohlc;
import com.github.lesach.indicator.SingleDoubleSerie;
import com.github.lesach.strategy.ETimeSerieResolutionType;
import com.github.lesach.strategy.MeasureModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface IndicatorProviderInterface {

    /// <summary>
    /// Normalize an compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="resolution"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    List<MeasureModel> ComputeIndicator(SerieBase reference,
                                               ETimeSerieResolutionType resolution,
                                               Indicator indicator);


    /// <summary>
    /// Normalize an compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="resolution"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    <T extends MeasureModel> List<MeasureModel> ComputeIndicator(List<T> reference,
                                                                        ETimeSerieResolutionType resolution,
                                                                        Indicator indicator);

    /// <summary>
    /// Compute indicator
    /// </summary>
    /// <param name="reference"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    List<MeasureModel> Compute(List<MeasureModel> ohlcs, Indicator indicator);

    /// <summary>
    /// Compute indicator
    /// </summary>
    /// <param name="ohlcs"></param>
    /// <param name="indicator"></param>
    /// <returns></returns>
    List<BigDecimal> ComputeIndicator(List<BigDecimal> ohlcs, Indicator indicator);

}
