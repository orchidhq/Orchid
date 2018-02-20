package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
public final class StringArrayOptionExtractor extends OptionExtractor<String> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;

    @Inject
    public StringArrayOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter) {
        super(10);
        this.contextProvider = contextProvider;
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(String[].class);
    }

    public String getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public String getOption(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting String not supported from this extractor, try StringOptionExtractor instead");
    }

    @Override
    public String getDefaultValue(Field field) {
        throw new UnsupportedOperationException("Extracting String not supported from this extractor, try StringOptionExtractor instead");
    }

    public String describeDefaultValue(Field field) {
        String[] value = getArrayDefaultValue(field);

        if(value == null) {
            return "null";
        }
        else if(value.length == 0) {
            return "[]";
        }
        else {
            return "[\"" + String.join("\", \"", value) + "\"]";
        }
    }

    @Override
    public String[] getArrayDefaultValue(Field field) {
        if(field.isAnnotationPresent(StringDefault.class)) {
            return field.getAnnotation(StringDefault.class).value();
        }
        return new String[0];
    }

    @Override
    public List<String> getList(Field field, JSONObject options, String key) {
        JSONArray array = new JSONArray();

        if((options.has(key) && options.get(key) instanceof JSONArray)) {
            array = options.getJSONArray(key);
        }
        else if((options.has(key) && options.get(key) instanceof String[])) {
            String[] strings = (String[]) options.get(key);
            for(String s : strings) {
                array.put(s);
            }
        }

        if(array.length() == 0) {
            String[] defaultValue = getArrayDefaultValue(field);
            for(String s : defaultValue) {
                array.put(s);
            }
        }

        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        List<String> list = this.getList(field, options, key);
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }
}
