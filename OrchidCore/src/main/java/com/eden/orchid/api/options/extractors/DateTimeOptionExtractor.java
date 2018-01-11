package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 */
public final class DateTimeOptionExtractor extends OptionExtractor<LocalDateTime> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public DateTimeOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(10);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalDateTime.class);
    }

    public LocalDateTime getValue(Object object) {
        String dateTimeString = converter.convert(object).second;

        try {
            return LocalDateTime.parse(dateTimeString);
        }
        catch (DateTimeParseException e) {
            Clog.e(e.getMessage(), e);
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
