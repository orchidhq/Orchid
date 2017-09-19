package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.DoubleConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.DoubleDefault;
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
 * | Field Type | Annotation     | Default Value             |
 * |------------|----------------|---------------------------|
 * | double     | @DoubleDefault | Annotation value() or 0.0 |
 * | Double     | @DoubleDefault | Annotation value() or 0.0 |
 */
public class DoubleOptionExtractor implements OptionExtractor<Double> {

    private DoubleConverter converter;

    @Inject
    public DoubleOptionExtractor(DoubleConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(double.class)
                || clazz.equals(Double.class)
                || clazz.equals(double[].class)
                || clazz.equals(Double[].class);
    }

    public double getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public Double getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            return getValue(options.get(key));
        }
        else if(field.isAnnotationPresent(DoubleDefault.class)) {
            return field.getAnnotation(DoubleDefault.class).value();
        }
        else {
            return 0.0;
        }
    }

    @Override
    public List<Double> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getDouble(i));
        }
        return list;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        List<Double> list = this.getList(field, options, key);

        if (field.getType().equals(double[].class)) {
            double[] array = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            return array;
        }
        else if (field.getType().equals(Double[].class)) {
            Double[] array = new Double[list.size()];
            list.toArray(array);
            return array;
        }

        return null;
    }
}
