package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;

/**
 * ### Source Types
 *
 * | Item Type  | Coercion |
 * |------------|----------|
 * | JSONObject | direct   |
 * | Map        | new JSONObject from map |
 *
 *
 * ### Destination Types
 *
 * | Field Type | Annotation  | Default Value |
 * |------------|-------------|---------------|
 * | JSONObject | none        | null          |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class JSONObjectOptionExtractor extends OptionExtractor<JSONObject> {

    private final FlexibleMapConverter converter;

    @Inject
    public JSONObjectOptionExtractor(FlexibleMapConverter converter) {
        super(10);
        this.converter = converter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(JSONObject.class);
    }

    @Override
    public JSONObject getOption(Field field, Object sourceObject, String key) {
        return new JSONObject(converter.convert(sourceObject));
    }

    @Override
    public JSONObject getDefaultValue(Field field) {
        return new JSONObject();
    }

}
