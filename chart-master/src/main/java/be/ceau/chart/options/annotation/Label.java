package be.ceau.chart.options.annotation;

import be.ceau.chart.color.Color;

public class Label {
    /*
     backgroundColor: "red",
              content: "Test Label",
              enabled: true
     */
    private Color backgroundColor;
    private String content;
    private Boolean enabled;
    private Double rotation;
    private Font font;
    // Padding of label to add left/right, default below
    private Integer xPadding;
    // Padding of label to add top/bottom, default below
    private Integer yPadding;

    // Radius of label rectangle, default below
    private Integer cornerRadius;
    // Anchor position of label on line, can be one of: top, bottom, left, right, center. Default below.
    private String position;

    // Adjustment along x-axis (left-right) of label relative to above number (can be negative)
    // For horizontal lines positioned left or right, negative values move
    // the label toward the edge, and positive values toward the center.
    private Integer xAdjust;

    // Adjustment along y-axis (top-bottom) of label relative to above number (can be negative)
    // For vertical lines positioned top or bottom, negative values move
    // the label toward the edge, and positive values toward the center.
    private Integer yAdjust;

}
