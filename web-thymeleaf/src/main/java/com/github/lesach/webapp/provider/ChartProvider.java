package com.github.lesach.webapp.provider;



import be.ceau.chart.options.elements.Fill;
import com.github.lesach.client.DPriceHistory;
import com.github.lesach.strategy.MeasureModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import be.ceau.chart.Chart;
import be.ceau.chart.LineChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.enums.BorderCapStyle;
import be.ceau.chart.enums.BorderJoinStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.lesach.strategy.engine.EngineProduct.getMeasureModels;

@Service
public class ChartProvider implements IChartProvider {

    private static final Logger logger = LogManager.getLogger(ChartProvider.class);

    @Override
    public Chart createChart(DPriceHistory priceHistory) {
        LineChart chart = new LineChart();
        chart.setData(createLineData(priceHistory));
        logger.info("Chart is" + (chart.isDrawable() ? " " : " NOT ") + "drawable");
        return chart;
    }


    private LineData createLineData(DPriceHistory priceHistory) {
        List<MeasureModel> values = getMeasureModels(priceHistory);
        values.sort(Comparator.comparing(MeasureModel::getDateTime));
        DateTimeFormatter dateTimeFormatter = priceHistory.getDateTimeFormatter();
        return new LineData()
                .addDataset(createLineDataset(values, priceHistory.getProduct().getName() + " (" + priceHistory.getProduct().getIsin() + ")"))
                .setLabels(values.stream().map(v -> v.getDateTime().format(dateTimeFormatter)).collect(Collectors.toList()));

    }

    @Override
    public LineDataset createLineDataset(List<? extends MeasureModel> values, String label) {
        return createLineDatasetFrom(values.stream().map(MeasureModel::getValue).collect(Collectors.toList()), label);
    }

    @Override
    public LineDataset createLineDatasetFrom(List<BigDecimal> values, String label) {
        return new LineDataset()
                .setLabel(label)
                .setFill(new Fill<Boolean>(true))
                .setLineTension(0.1f)
                .setBackgroundColor(new Color(75, 192, 192, 0.4))
                .setBorderColor(new Color(75, 192, 192, 1))
                .setBorderCapStyle(BorderCapStyle.BUTT)
                .setBorderDashOffset(0.0f)
                .setBorderJoinStyle(BorderJoinStyle.MITER)
                .addPointBorderColor(new Color(75, 192, 192, 1))
                .addPointBackgroundColor(new Color(255, 255, 255, 1))
                .addPointBorderWidth(1)
                .addPointHoverRadius(5)
                .addPointHoverBackgroundColor(new Color(75, 192, 192, 1))
                .addPointHoverBorderColor(new Color(220, 220, 220, 1))
                .addPointHoverBorderWidth(2)
                .addPointRadius(1)
                .addPointHitRadius(10)
                .setSpanGaps(false)
                .setData(values);
    }
    /*
    options: {
      responsive: true,
      title: {
        display: true,
        text: "Chart.js Combo Bar Line Chart"
      },
      tooltips: {
        mode: "index",
        intersect: true
      },
      annotation: {
        events: ["click"],
        annotations: [
          {
            drawTime: "afterDatasetsDraw",
            id: "hline",
            type: "line",
            mode: "horizontal",
            scaleID: "y-axis-0",
            value: randomScalingFactor(),
            borderColor: "black",
            borderWidth: 5,
            label: {
              backgroundColor: "red",
              content: "Test Label",
              enabled: true
            },
            onClick: function(e) {
              // The annotation is is bound to the `this` variable
              console.log("Annotation", e.type, this);
            }
          },
          {
            drawTime: "beforeDatasetsDraw",
            type: "box",
            xScaleID: "x-axis-0",
            yScaleID: "y-axis-0",
            xMin: "February",
            xMax: "April",
            yMin: randomScalingFactor(),
            yMax: randomScalingFactor(),
            backgroundColor: "rgba(101, 33, 171, 0.5)",
            borderColor: "rgb(101, 33, 171)",
            borderWidth: 1,
            onClick: function(e) {
              console.log("Box", e.type, this);
            }
          }
        ]
      }
    }
     */
}