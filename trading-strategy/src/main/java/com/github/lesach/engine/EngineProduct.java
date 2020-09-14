

package com.github.lesach.engine;

import com.github.lesach.*;
import com.github.lesach.serie.Indicator;
import com.github.lesach.serie.IndicatorProvider;
import com.github.lesach.serie.SerieKey;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class EngineProduct
{
    public DProductDescription Product;
    public ETimeSerieResolutionType Resolution;
    public List<EngineSerieValues> Series = new ArrayList<>();
    private final IndicatorProvider indicatorProvider = new IndicatorProvider();

    /// <summary>
    ///
    /// </summary>
    /// <param name="price"></param>
    public void UpdatePrice(DPrice price)
    {
        AddValue(EIndicatorType.Ask, price.getRefreshTime(), price.getAsk());
        AddValue(EIndicatorType.Bid, price.getRefreshTime(), price.getBid());
        AddValue(EIndicatorType.Last, price.getRefreshTime(), price.getLast());
        RefreshIndicators();
    }

    /// <summary>
    /// Compute indicators
    /// </summary>
    public void RefreshIndicators()
    {
        EngineSerieValues lastSerieValues = Series.stream().filter(ser -> ser.Key.getIndicator().IndicatorType == EIndicatorType.Last).findFirst().orElse(null);
        if (lastSerieValues != null)
        {
            Series.stream().filter(e -> (e.Key.getIndicator().IndicatorType.getValue() > 10) && (e.LastUpdate.isBefore(lastSerieValues.LastUpdate)))
                .forEach(engineSerieValues ->
            {
                indicatorProvider.ComputeIndicator(lastSerieValues.Values, Resolution, engineSerieValues.Key.getIndicator())
                        .stream()
                        .filter(i -> (engineSerieValues.Values.size() == 0) || (i.getDateTime().isAfter(engineSerieValues.Values.stream().map(MeasureModel::getDateTime)
                        .max(LocalDateTime::compareTo).orElse(LocalDateTime.MIN))))
                        .forEach(m ->
                {
                    engineSerieValues.AddValue(m.getDateTime(), m.getValue());
                });
            });
        }
    }

    /// <summary>
    /// Initialize
    /// </summary>
    public void Initialize(LocalDateTime start, LocalDateTime end)
    {
        DPriceHistory priceHistory = DeGiroClient.getInstance().GetPriceHistory(Product.getVwdIdentifierType(),
                        Product.getVwdId(),
                        start,
                        end,
                        Resolution.toString());
        DPriceHistorySerie priceSerieData = null;
        DPriceHistorySerie productSerieData = null;
        for (DPriceHistorySerie s : priceHistory.series)
        {
            if (s.data != null)
            {
                if (s.data.prices != null)
                    priceSerieData = s;
                if (s.data.product != null)
                    productSerieData = s;
            }
        }
        if (productSerieData != null && priceSerieData != null)
        {
            // If prices are present
            if (productSerieData.data.product.windowFirst != null)
            {

                EngineSerieValues engineSerieValues = Series.stream().filter(d -> d.Key.getIndicator().IndicatorType == EIndicatorType.Last).findFirst().orElse(null);
                if (engineSerieValues == null)
                {
                    engineSerieValues = new EngineSerieValues(new SerieKey() {{ setProduct(Product);
                        setIndicator(new Indicator(EIndicatorType.Last)); }}, Resolution);
                    Series.add(engineSerieValues);
                }
                List<MeasureModel> values = getMeasureModels(priceHistory);
                values.sort((m1, m2) -> m1.getDateTime().compareTo(m2.getDateTime()));
                for (MeasureModel measureModel : values)
                    engineSerieValues.AddValue(measureModel.getDateTime(), measureModel.getValue());
            }
        }
        RefreshIndicators();
    }

    public static List<MeasureModel> getMeasureModels(DPriceHistory priceHistory)
    {
        List<MeasureModel> result = new ArrayList<MeasureModel>();
        DPriceHistorySerie priceSerieData = null;
        DPriceHistorySerie productSerieData = null;
        for (DPriceHistorySerie s : priceHistory.series)
        {
            if (s.data != null)
            {
                if (s.data.prices != null)
                    priceSerieData = s;
                if (s.data.product != null)
                    productSerieData = s;
            }
        }
        if (productSerieData != null && priceSerieData != null)
        {
            // If prices are present
            if (productSerieData.data.product.windowFirst !=null)
            {
                Map<BigDecimal, List<BigDecimal[]>> m = priceSerieData.data.prices
                        .stream()
                        .collect(Collectors.groupingBy(a -> a[0]));

                List<List<BigDecimal>> averagePrices = m.entrySet()
                        .stream()
                        .map(c -> Arrays.asList(c.getKey(), c.getValue().stream().map(d -> d[1]).reduce(BigDecimal.ZERO, BigDecimal::add)
                                .divide(BigDecimal.valueOf(c.getValue().size()), MathContext.DECIMAL64)))
                        .sorted((d1, d2) -> d1.get(0).compareTo(d2.get(0)))
                        .collect(Collectors.toList());

                for (List<BigDecimal> d : averagePrices)
                {
                    DPriceHistorySerie t = productSerieData;
                    result.add(new MeasureModel()
                    {{
                        setDateTime(t.data.product.windowStart.plusSeconds(d.get(0).setScale(0, RoundingMode.HALF_UP).longValue()));
                        setValue(d.get(1));
                    }});
                }
            }
        }
        return result;
    }

    /// <summary>
    /// A a value at the end of the serie
    /// </summary>
    /// <param name="EIndicatorType"></param>
    /// <param name="dateTime"></param>
    /// <param name="price"></param>
    private void AddValue(EIndicatorType EIndicatorType, LocalDateTime dateTime, BigDecimal price)
    {
        if (price.compareTo(BigDecimal.ZERO) > 0)
        {
            EngineSerieValues engineSerieValues = Series.stream().filter(d -> d.Key.getIndicator().IndicatorType == EIndicatorType).findFirst().orElse(null);
            if (engineSerieValues != null)
                engineSerieValues.AddValue(dateTime, price);
        }
    }
}

