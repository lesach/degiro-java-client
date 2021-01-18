
package be.ceau.chart.options.annotation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
"annotation": {
        "events": ["click"],
        "annotations": [
          {

        ]
      }
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Annotation {
    public String drawTime;
    private final List<String> events = new ArrayList<>();
    private final List<AnnotationElement> annotations = new ArrayList<>();

    public Annotation() {

    }

}
