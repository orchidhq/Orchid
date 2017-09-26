package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.BooleanConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

/**
 * ### Destination Types
 *
 * | Field Type | Annotation      | Default Value               |
 * |------------|-----------------|-----------------------------|
 * | boolean    | @BooleanDefault | Annotation value() or false |
 * | Boolean    | @BooleanDefault | Annotation value() or false |
 */
public final class BooleanOptionExtractor implements OptionExtractor<Boolean> {

    private final BooleanConverter converter;

    @Inject
    public BooleanOptionExtractor(BooleanConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(boolean.class)
                || clazz.equals(Boolean.class);
    }

    public boolean getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public Boolean getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            return getValue(options.get(key));
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
