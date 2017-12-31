package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.FloatConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.FloatDefault;
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
 * | Field Type | Annotation    | Default Value              |
 * |------------|---------------|----------------------------|
 * | float      | @FloatDefault | Annotation value() or 0.0f |
 * | Float      | @FloatDefault | Annotation value() or 0.0f |
 */
public final class FloatOptionExtractor extends OptionExtractor<Float> {

    private final FloatConverter converter;

    @Inject
    public FloatOptionExtractor(FloatConverter converter) {
        super(50);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(float.class)
                || clazz.equals(Float.class)
                || clazz.equals(float[].class)
                || clazz.equals(Float[].class);
    }

    public float getValue(Object object) {
        return converter.convert(object).second;
    }

    @Override
    public Float getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            return getValue(options.get(key));
        }
        else {
            return getDefaultValue(field);
        }
    }

    @Override
    public Float getDefaultValue(Field field) {
        if(field.isAnnotationPresent(FloatDefault.class)) {
            return field.getAnnotation(FloatDefault.class).value();
        }
        else {
            return 0.0f;
        }
    }

    @Override
    public List<Float> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<Float> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add((float) array.getDouble(i));
        }
        return list;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        List<Float> list = this.getList(field, options, key);

        if (field.getType().equals(float[].class)) {
            float[] array = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = list.get(i);
            }
            return array;
        }
        else if (field.getType().equals(Float[].class)) {
            Float[] array = new Float[list.size()];
            list.toArray(array);
            return array;
        }

        return null;
    }
}
