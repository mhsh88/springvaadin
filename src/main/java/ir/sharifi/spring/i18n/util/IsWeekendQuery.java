package ir.sharifi.spring.i18n.util;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;

public class IsWeekendQuery implements TemporalQuery<Boolean> {

    @Override
    public Boolean queryFrom(TemporalAccessor temporal) {
        return temporal.get(ChronoField.DAY_OF_WEEK) >= 5;
    }
}
