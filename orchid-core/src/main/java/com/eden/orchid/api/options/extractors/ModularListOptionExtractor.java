package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.FlexibleIterableConverter;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.theme.components.ModularList;

import javax.inject.Provider;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | String       | @StringDefault | Annotation value() or null |
 * | String[]     | none           | Empty String[]             |
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
        OrchidContext context = contextProvider.get();

        final String typeKey;
        final String valueKey;
        if(field.isAnnotationPresent(ImpliedKey.class)) {
            ImpliedKey impliedKey = field.getAnnotation(ImpliedKey.class);
            typeKey = impliedKey.typeKey();
            valueKey = impliedKey.valueKey();
        }
        else {
            typeKey = null;
            valueKey = null;
        }

        Iterable iterable = iterableConverter.convert(field.getType(), sourceObject, typeKey, valueKey).second;

        List<Map<String, Object>> jsonArray = new ArrayList<>();
        for(Object o : iterable) {
            Map<String, Object> map = (Map<String, Object>) mapConverter.convert(field.getType(), o).second;
            jsonArray.add(map);
        }

        if(jsonArray.size() > 0) {
            ModularList modularList = (ModularList) contextProvider.get().resolve(field.getType());
            modularList.initialize(context, jsonArray);
            return modularList;
        }

        return null;
    }

    @Override
    public ModularList getDefaultValue(Field field) {
        OrchidContext context = contextProvider.get();
        ModularList modularList = (ModularList) context.resolve(field.getType());
        modularList.initialize(context, new ArrayList<>());
        return modularList;
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "Empty " + field.getType().getSimpleName();
    }

}
