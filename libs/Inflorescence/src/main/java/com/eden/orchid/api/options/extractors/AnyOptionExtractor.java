package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class AnyOptionExtractor extends OptionExtractor<Object> {

    @Inject
    public AnyOptionExtractor() {
        super(1);
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return true;
    }

    @Override
    public Object getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            return options.get(key);
        }
        else {
            return getDefaultValue(field);
        }
    }

    @Override
    public Object getDefaultValue(Field field) {
        return null;
    }

    @Override public List<Object> getList(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting List<Object> not supported");
    }

    @Override public Object getArray(Field field, JSONObject options, String key) {
        throw new UnsupportedOperationException("Extracting Object[] not supported");
    }
}
