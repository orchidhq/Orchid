package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.options.OptionExtractor;

import javax.inject.Inject;
import java.lang.reflect.Field;
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
public final class DateTimeOptionExtractor extends OptionExtractor<LocalDateTime> {

    private final DateTimeConverter converter;

    @Inject
    public DateTimeOptionExtractor(DateTimeConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalDateTime.class);
    }

    @Override
    public LocalDateTime getOption(Field field, Object sourceObject, String key) {
        return converter.convert(sourceObject).second;
    }

    @Override
    public LocalDateTime getDefaultValue(Field field) {
        return LocalDateTime.now();
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "now (yyyy-mm-dd HH:MM:SS)";
    }

}
