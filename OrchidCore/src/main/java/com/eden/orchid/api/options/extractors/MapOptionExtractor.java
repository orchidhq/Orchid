package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ImpliedKey;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class MapOptionExtractor extends OptionExtractor<Map> {

    private final FlexibleMapConverter mapConverter;

    @Inject
    public MapOptionExtractor(FlexibleMapConverter mapConverter) {
        super(2);
        this.mapConverter = mapConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public Map getOption(Field field, Object sourceObject, String key) {
        final String typeKey;
        if(field.isAnnotationPresent(ImpliedKey.class)) {
            ImpliedKey impliedKey = field.getAnnotation(ImpliedKey.class);
            typeKey = impliedKey.typeKey();
        }
        else {
            typeKey = null;
        }

        return mapConverter.convert(field.getType(), sourceObject, typeKey).second;
    }

    @Override
    public Map getDefaultValue(Field field) {
        return new HashMap();
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "{}";
    }
}
