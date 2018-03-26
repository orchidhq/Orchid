package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    public LocalDate getValue(Object object) {
        EdenPair<Boolean, LocalDateTime> dateTime = converter.convert(object);

        if(dateTime.first) {
            return dateTime.second.toLocalDate();
        }

        return null;
    }

    @Override
    public LocalDate getOption(Field field, JSONObject options, String key) {
        LocalDate fieldValue = null;
        if(options.has(key)) {
            LocalDate value = getValue(options.get(key));
            if(value != null) {
                fieldValue = value;
            }
        }

        if(fieldValue == null) {
            fieldValue = getDefaultValue(field);
        }

        return fieldValue;
    }

    @Override
    public LocalDate getDefaultValue(Field field) {
        return LocalDate.now();
    }

    @Override
    public List<LocalDate> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<LocalDate> not supported");
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting LocalDate[] not supported");
    }
}
