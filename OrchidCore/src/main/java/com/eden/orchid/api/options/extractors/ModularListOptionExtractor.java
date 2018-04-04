package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.FlexibleIterableConverter;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ModularListConfig;
import com.eden.orchid.api.theme.components.ModularList;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class ModularListOptionExtractor extends OptionExtractor<ModularList> {

    private final Provider<OrchidContext> contextProvider;
    private final FlexibleIterableConverter iterableConverter;
    private final FlexibleMapConverter mapConverter;

    @Inject
    public ModularListOptionExtractor(Provider<OrchidContext> contextProvider, FlexibleIterableConverter iterableConverter, FlexibleMapConverter mapConverter) {
        super(50);
        this.contextProvider = contextProvider;
        this.iterableConverter = iterableConverter;
        this.mapConverter = mapConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return ModularList.class.isAssignableFrom(clazz);
    }

    @Override
    public ModularList getOption(Field field, Object sourceObject, String key) {
        String objectKeyName = (field.isAnnotationPresent(ModularListConfig.class))
                ? field.getAnnotation(ModularListConfig.class).objectKeyName()
                : "type";

        Iterable iterable = iterableConverter.convert(sourceObject, objectKeyName).second;

        JSONArray jsonArray = new JSONArray();
        for(Object o : iterable) {
            Map map = mapConverter.convert(o).second;
            jsonArray.put(new JSONObject(map));
        }

        if(jsonArray.length() > 0) {
            ModularList modularList = (ModularList) contextProvider.get().getInjector().getInstance(field.getType());
            modularList.initialize(jsonArray);
            return modularList;
        }

        return null;
    }

    @Override
    public ModularList getDefaultValue(Field field) {
        ModularList modularList = (ModularList) contextProvider.get().getInjector().getInstance(field.getType());
        modularList.initialize(new JSONArray());
        return modularList;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "Empty " + field.getType().getSimpleName();
    }

}
