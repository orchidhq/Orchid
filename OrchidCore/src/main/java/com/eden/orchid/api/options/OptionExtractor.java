package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.registration.Prioritized;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Extract classes of a specific type from a JSON-like structure.
 *
 * @since v1.0.0
 * @orchidApi extensible
 * @param <T> the type of object that this Extractor will return
 */
public abstract class OptionExtractor<T> extends Prioritized {

    public OptionExtractor(int priority) {
        super(priority);
    }

    public abstract boolean acceptsClass(Class clazz);

    public abstract T getOption(Field field, JSONObject options, String key);

    public abstract T getDefaultValue(Field field);

    public String describeDefaultValue(Field field) {
        T value = getDefaultValue(field);

        if(value == null) {
            return "null";
        }
        else if(value instanceof String) {
            if(EdenUtils.isEmpty(value.toString())) {
                return "empty string";
            }
            else {
                return Clog.format("\"{}\"", value);
            }
        }
        else {
            return value.toString();
        }
    }

    public List<T> getList(Field field, JSONObject options, String key) {
        return null;
    }

    public Object getArray(Field field, JSONObject options, String key) {
        return null;
    }

}
