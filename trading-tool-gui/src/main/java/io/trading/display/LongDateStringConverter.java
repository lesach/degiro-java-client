package io.trading.display;

import io.trading.utils.Format;
import javafx.util.StringConverter;

import java.util.Date;

public class LongDateStringConverter extends StringConverter<Number> {

    @Override
    public String toString(Number d) {
        return Format.formatDate(d.longValue());
    }

    @Override
    public Number fromString(String d) {
        Date date = Format.parseDate(d);
        if (date == null)
            return null;
        else
            return date.getTime();
    }
}