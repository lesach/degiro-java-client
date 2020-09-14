
package com.github.lesach;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MeasureModel
{
    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }

    public BigDecimal getValue() {
        return Value;
    }

    public void setValue(BigDecimal value) {
        Value = value;
    }

    private LocalDateTime DateTime;
    private BigDecimal Value;
}
