package com.eden.orchid.api.options;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public interface OptionExtractor<T> {

    boolean acceptsClass(Class clazz);

    T getOption(Field field, JSONObject options, String key);
    List<T> getList(Field field, JSONObject options, String key);
    Object getArray(Field field, JSONObject options, String key);
}
