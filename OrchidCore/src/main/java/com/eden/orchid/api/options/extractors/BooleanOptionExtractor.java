package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class BooleanOptionExtractor implements OptionExtractor<Boolean> {

    @Override
    public boolean acceptsClass(Class<?> clazz) {
        return clazz.equals(boolean.class)
                || clazz.equals(Boolean.class);
    }

    @Override
    public Boolean getOption(Field field, JSONObject options, String key) {
        if(options.has(key) && options.get(key) instanceof Boolean) {
            return options.getBoolean(key);
        }
        else if(field.isAnnotationPresent(BooleanDefault.class)) {
            return field.getAnnotation(BooleanDefault.class).value();
        }
        else {
            return false;
        }
    }

    @Override
    public List<Boolean> getList(Field field, JSONObject options, String key) {
        return null;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        return null;
    }
}
