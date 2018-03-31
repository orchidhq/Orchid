package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.options.OptionExtractor;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class DateOptionExtractor extends OptionExtractor<LocalDate> {

    private final DateTimeConverter converter;

    @Inject
    public DateOptionExtractor(DateTimeConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalDate.class);
    }

    @Override
    public LocalDate getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, LocalDateTime> dateTime = converter.convert(sourceObject);

        if(dateTime.first) {
            return dateTime.second.toLocalDate();
        }

        return null;
    }

    @Override
    public LocalDate getDefaultValue(Field field) {
        return LocalDate.now();
    }

}
