package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.TimeConverter;
import com.eden.orchid.api.options.OptionExtractor;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalTime;

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
public final class TimeOptionExtractor extends OptionExtractor<LocalTime> {

    private final TimeConverter converter;

    @Inject
    public TimeOptionExtractor(TimeConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalTime.class);
    }


    @Override
    public LocalTime getOption(Field field, Object sourceObject, String key) {
        EdenPair<Boolean, LocalTime> dateTime = converter.convert(sourceObject);

        if(dateTime.first) {
            return dateTime.second;
        }

        return null;
    }

    @Override
    public LocalTime getDefaultValue(Field field) {
        return LocalTime.now();
    }

}
