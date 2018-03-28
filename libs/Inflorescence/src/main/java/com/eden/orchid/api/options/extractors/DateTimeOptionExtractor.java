package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.DateTimeConverter;
import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
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

    public LocalDateTime getValue(Object object) {
        EdenPair<Boolean, LocalDateTime> dateTime = converter.convert(object);

        if(dateTime.first) {
            return dateTime.second;
        }

        return null;
    }

    @Override
    public LocalDateTime getOption(Field field, JSONObject options, String key) {
        LocalDateTime fieldValue = null;
        if(options.has(key)) {
            LocalDateTime value = getValue(options.get(key));
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
    public LocalDateTime getDefaultValue(Field field) {
        return LocalDateTime.now();
    }

    @Override
    public List<LocalDateTime> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<LocalDateTime> not supported");
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting LocalDateTime[] not supported");
    }
}
