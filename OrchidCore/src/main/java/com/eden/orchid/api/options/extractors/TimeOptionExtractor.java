package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
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
public final class TimeOptionExtractor extends OptionExtractor<LocalTime> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public TimeOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(10);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalTime.class);
    }

    public LocalTime getValue(Object object) {
        String timeString = converter.convert(object).second;

        try {
            return LocalTime.parse(timeString);
        }
        catch (DateTimeParseException e) {
            Clog.e(e.getMessage(), e);
        }

        return null;
    }

    @Override
    public LocalTime getOption(Field field, JSONObject options, String key) {
        LocalTime fieldValue = null;
        if(options.has(key)) {
            LocalTime value = getValue(options.get(key));
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
    public LocalTime getDefaultValue(Field field) {
        return LocalTime.now();
    }

    @Override
    public List<LocalTime> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<LocalTime> not supported");
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting LocalTime[] not supported");
    }
}
