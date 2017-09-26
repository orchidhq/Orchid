package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.IntegerConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.IntDefault;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * ### Source Types
 *
 * | Item Type | Coercion |
 * |-----------|----------|
 * | number    | direct   |
 * | string    | parse as number |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value           |
 * |------------|-------------|-------------------------|
 * | int        | @IntDefault | Annotation value() or 0 |
 * | Integer    | @IntDefault | Annotation value() or 0 |
 */
public final class IntOptionExtractor implements OptionExtractor<Integer> {

    private final IntegerConverter converter;

    @Inject
    public IntOptionExtractor(IntegerConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(int.class)
                || clazz.equals(Integer.class)
                || clazz.equals(int[].class)
                || clazz.equals(Integer[].class);
    }

    public int getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public Integer getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            return getValue(options.get(key));
        }
        else if(field.isAnnotationPresent(IntDefault.class)) {
            return field.getAnnotation(IntDefault.class).value();
        }
        else {
            return 0;
        }
    }

    @Override
    public List<Integer> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getInt(i));
        }
        return list;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        List<Integer> list = this.getList(field, options, key);

        if (field.getType().equals(int[].class)) {
            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            return array;
        }
        else if (field.getType().equals(Integer[].class)) {
            Integer[] array = new Integer[list.size()];
            list.toArray(array);
            return array;
        }

        return null;
    }
}
