package com.github.lesach.webapp.provider;

import be.ceau.chart.Chart;
import be.ceau.chart.dataset.LineDataset;
import com.github.lesach.client.DPriceHistory;
import com.github.lesach.strategy.MeasureModel;

import java.math.BigDecimal;
import java.util.List;

public interface IChartProvider {

    /**
     * Abstract superclass for chart type specific test
     */
    Chart createChart(DPriceHistory priceHistory);

    LineDataset createLineDataset(List<MeasureModel> values, String label);

    LineDataset createLineDatasetFrom(List<BigDecimal> values, String label);

}
