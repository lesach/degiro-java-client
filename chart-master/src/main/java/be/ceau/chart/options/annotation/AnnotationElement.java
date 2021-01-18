package be.ceau.chart.options.annotation;

import be.ceau.chart.color.Color;
import be.ceau.chart.javascript.JavaScriptFunction;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class AnnotationElement {
    /*

    "drawTime": "beforeDatasetsDraw",
    "type": "box",
    "xScaleID": "x-axis-0",
    "yScaleID": "y-axis-0",
    "xMin": "February",
    "xMax": "April",
    "yMin": "randomScalingFactor()",
    "yMax": "randomScalingFactor()",
    "backgroundColor": "rgba(101, 33, 171, 0.5)",
    "borderColor": "rgb(101, 33, 171)",
    "borderWidth": 1,
    "onClick": "function(e) { console.log(Box, e.type, this); }"


     */

    // Should be one of: afterDraw, afterDatasetsDraw, beforeDatasetsDraw
    private String drawTime;
    private String id;
    private String mode;
    // Optional value at which the line draw should end
    private Integer endValue;
    // Line dash
    // https://developer.mozilla.org/en-US/docs/Web/API/CanvasRenderingContext2D/setLineDash
    private List<Integer> borderDash;// [2, 2],
    private Integer borderDashOffset;
    // Line Dash Offset
    // Line, box, point, ellipse
    private String type;
    private String scaleID;
    private String xScaleID;
    private String yScaleID;
    private Label label;
    private String xMin;
    private String xMax;
    private String yMin;
    private String yMax;
    private Color backgroundColor;
    private Color borderColor;
    private String borderWidth;
    // Radius of the point, default below
    private Integer radius;
    private JavaScriptFunction onEnter;
    private JavaScriptFunction onLeave;
    private JavaScriptFunction onDblClick;
    private JavaScriptFunction onClick;
}
