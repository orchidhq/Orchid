package com.eden.orchid.api.options.extractors;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.StringDefault;
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
 */
public final class StringOptionExtractor implements OptionExtractor<String> {

    private final StringConverter converter;

    @Inject
    public StringOptionExtractor(StringConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(String.class)
                || clazz.equals(String[].class);
    }

    public String getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public String getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            EdenPair<Boolean, String> value = converter.convert(options.get(key));
            if(value.first) {
                return value.second;
            }
        }

        if(field.isAnnotationPresent(StringDefault.class)) {
            return field.getAnnotation(StringDefault.class).value();
        }

        return "";
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
