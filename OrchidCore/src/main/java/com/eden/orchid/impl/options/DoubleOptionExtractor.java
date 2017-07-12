package com.eden.orchid.impl.options;

import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DoubleOptionExtractor implements OptionExtractor<Double> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(double.class)
                || clazz.equals(Double.class)
                || clazz.equals(double[].class)
                || clazz.equals(Double[].class);
    }

    @Override
    public Double getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof Double) {
            return options.getDouble(key);
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
