package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * | Input  | Result                 | Converter |
 * |--------|------------------------|-----------|
 * | number | that number as float   |           |
 * | string | parsed number as float | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class TimeConverter implements TypeConverter<LocalTime> {

    private final DateTimeConverter converter;

    @Inject
    public TimeConverter(DateTimeConverter converter) {
        this.converter = converter;
    }

    @Override
    public Class<LocalTime> resultClass() {
        return LocalTime.class;
    }

    @Override
    public EdenPair<Boolean, LocalTime> convert(Object object) {
        EdenPair<Boolean, LocalDateTime> dateTime = converter.convert(object);

        return new EdenPair<>(dateTime.first, dateTime.second.toLocalTime());
    }

}
