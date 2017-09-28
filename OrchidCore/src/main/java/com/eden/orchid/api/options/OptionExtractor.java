package com.eden.orchid.api.options;

import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public abstract class OptionExtractor<T> extends Prioritized {

    public OptionExtractor(int priority) {
        super(priority);
    }

    public abstract boolean acceptsClass(Class clazz);

    public abstract T getOption(Field field, JSONObject options, String key);

    public List<T> getList(Field field, JSONObject options, String key) {
        return null;
    }

    public Object getArray(Field field, JSONObject options, String key) {
        return null;
    }

}
