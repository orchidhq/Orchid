package com.eden.orchid.api.options.extractors;

import clog.Clog;
import clog.dsl.UtilsKt;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ImpliedKey;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.components.ModularType;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ### Destination Types
 *
 * | Field Type   | Annotation     | Default Value              |
 * |--------------|----------------|----------------------------|
 * | ModularType  | @StringDefault | Annotation value() or null |
 */
public final class ModularTypeOptionExtractor extends OptionExtractor<ModularType> {

    private final Provider<OrchidContext> contextProvider;
    private final FlexibleMapConverter mapConverter;

    @Inject
    public ModularTypeOptionExtractor(Provider<OrchidContext> contextProvider, FlexibleMapConverter mapConverter) {
        super(50);
        this.contextProvider = contextProvider;
        this.mapConverter = mapConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return ModularType.class.isAssignableFrom(clazz);
    }

    @Override
    public ModularType getOption(Field field, Object sourceObject, String key) {
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

        Map<String, Object> data = mapConverter.convert(field.getType(), sourceObject, typeKey).second;

        ModularType t = null;

        Object actualType = data.get(typeKey);
        if(actualType != null) {
            t = createModularType(field, actualType.toString(), data);
            if(t == null) {
                Clog.e(
                        "Could not find modular type [{}] for {} {} in class {}, falling back to default [{}]",
                        actualType,
                        field.getType().getSimpleName(),
                        field.getName(),
                        field.getDeclaringClass().getSimpleName(),
                        getDefaultType(field)
                );
            }
        }

        actualType = data.get(null);
        if(actualType != null) {
            t = createModularType(field, actualType.toString(), data);
            if(t == null) {
                Clog.e(
                        "Could not find modular type [{}] for {} {} in class {}, falling back to default [{}]",
                        actualType,
                        field.getType().getSimpleName(),
                        field.getName(),
                        field.getDeclaringClass().getSimpleName(),
                        getDefaultType(field)
                );
            }
        }

        return t;
    }

    @Override
    public ModularType getDefaultValue(Field field) {
        ModularType t = createModularType(field, getDefaultType(field), new HashMap<>());

        if(t == null) {
            throw new IllegalArgumentException(
                    UtilsKt.format(Clog.INSTANCE,
                            "No sensible default found for ModularType {} {} in class {}",
                            field.getType().getSimpleName(), field.getName(), field.getDeclaringClass().getSimpleName()
                    )
            );
        }
        else {
            return t;
        }
    }

    @Override
    public String describeDefaultValue(Field field) {
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

        return "Default " + field.getType().getSimpleName() + " of type '" + typeKey + "'";
    }

    private String getDefaultType(Field field) {
        if(field.isAnnotationPresent(StringDefault.class)) {
            String[] defaultValue = field.getAnnotation(StringDefault.class).value();
            if(defaultValue.length > 0) {
                return defaultValue[0];
            }
        }

        return "";
    }

    private ModularType createModularType(Field field, String selectedTypeKey, Map<String, Object> data) {
        Set<? extends ModularType> itemTypes = (Set<? extends ModularType>) contextProvider.get().resolveSet(field.getType());
        for (ModularType itemType : itemTypes) {
            if(itemType.getType().equals(selectedTypeKey)) {
                ModularType type = contextProvider.get().resolve(itemType.getClass());
                type.extractOptions(contextProvider.get(), data);

                return type;
            }
        }
        return null;
    }

}
