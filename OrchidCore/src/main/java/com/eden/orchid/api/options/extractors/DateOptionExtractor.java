package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.time.LocalDate;
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
public final class DateOptionExtractor extends OptionExtractor<LocalDate> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public DateOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(10);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(LocalDate.class);
    }

    public LocalDate getValue(Object object) {
        String dateString = converter.convert(object).second;

        try {
            return LocalDate.parse(dateString);
        }
        catch (DateTimeParseException e) {
            Clog.e(e.getMessage(), e);
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
