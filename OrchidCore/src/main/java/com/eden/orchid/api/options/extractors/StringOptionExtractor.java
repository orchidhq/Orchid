package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
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
public final class StringOptionExtractor extends OptionExtractor<String> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public StringOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(10);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(String.class);
    }

    public String getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public String getOption(Field field, JSONObject options, String key) {
        String fieldValue = null;
        if(options.has(key)) {
            EdenPair<Boolean, String> value = converter.convert(options.get(key));
            if(value.first) {
                fieldValue = value.second;
            }
        }

        if(fieldValue == null) {
            fieldValue = getDefaultValue(field);
        }

        if(fieldValue == null) {
            fieldValue = "";
        }

        return fieldValue;
    }

    @Override
    public String getDefaultValue(Field field) {
        if(field.isAnnotationPresent(StringDefault.class)) {
            String[] defaultValue = field.getAnnotation(StringDefault.class).value();
            if(defaultValue.length > 0) {
                EdenPair<Boolean, String> value = converter.convert(defaultValue[0]);
                if(value.first) {
                    return value.second;
                }
            }
        }
        return "";
    }

    public String[] getArrayDefaultValue(Field field) {
        return new String[0];
    }

    @Override
    public List<String> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<String> not supported, try String[] instead");
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting String[] not supported from this extractor, try StringArrayOptionExtractor instead");
    }
}
