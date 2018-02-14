package com.eden.orchid.api.converters;

import com.eden.common.util.EdenPair;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * | Input  | Result                 | Converter |
 * |--------|------------------------|-----------|
 * | number | that number as float   |           |
 * | string | parsed number as float | toString  |
 *
 * @since v1.0.0
 * @orchidApi converters
 */
public final class DateTimeConverter implements TypeConverter<LocalDateTime> {

    private final StringConverter stringConverter;

    @Inject
    public DateTimeConverter(StringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public Class<LocalDateTime> resultClass() {
        return LocalDateTime.class;
    }

    @Override
    public EdenPair<Boolean, LocalDateTime> convert(Object object) {
        if(object instanceof LocalDate) {
            return new EdenPair<>(true, ((LocalDate) object).atStartOfDay());
        }
        else if(object instanceof LocalTime) {
            return new EdenPair<>(true, ((LocalTime) object).atDate(LocalDate.now()));
        }
        else if(object instanceof LocalDateTime) {
            return new EdenPair<>(true, ((LocalDateTime) object));
        }
        else {
            String dateTimeString = stringConverter.convert(object).second;

            if(dateTimeString.equalsIgnoreCase("now")) {
                return new EdenPair<>(true, LocalDate.now().atStartOfDay());
            }

            try {
                return new EdenPair<>(true, LocalDateTime.parse(dateTimeString));
            }
            catch (DateTimeParseException e) { }

            try {
                return new EdenPair<>(true, LocalDate.parse(dateTimeString).atStartOfDay());
            }
            catch (DateTimeParseException e) { }
        }

        return new EdenPair<>(false, LocalDate.now().atStartOfDay());
    }

}
