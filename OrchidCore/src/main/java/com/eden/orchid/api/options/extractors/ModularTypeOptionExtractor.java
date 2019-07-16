package com.eden.orchid.api.options.extractors;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.FlexibleMapConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.annotations.ModularListConfig;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.components.ModularType;
import javax.inject.Provider;

import javax.inject.Inject;
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
 *
 * @since v1.0.0
 * @orchidApi optionTypes
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
        String objectKeyName = getObjectKeyName(field);

        Map<String, Object> data = mapConverter.convert(field.getType(), sourceObject, objectKeyName).second;

        ModularType t = null;

        Object typeKey = data.get(objectKeyName);
        if(typeKey != null) {
            t = createModularType(field, typeKey.toString(), data);
            if(t == null) {
                Clog.e(
                        "Could not find modular type [{}] for {} {} in class {}, falling back to default [{}]",
                        typeKey,
                        field.getType().getSimpleName(),
                        field.getName(),
                        field.getDeclaringClass().getSimpleName(),
                        getDefaultType(field)
                );
            }
        }

        typeKey = data.get(null);
        if(typeKey != null) {
            t = createModularType(field, typeKey.toString(), data);
            if(t == null) {
                Clog.e(
                        "Could not find modular type [{}] for {} {} in class {}, falling back to default [{}]",
                        typeKey,
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
                    Clog.format(
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
        return "Default " + field.getType().getSimpleName() + " of type '" + getObjectKeyName(field) + "'";
    }

    private String getObjectKeyName(Field field) {
        return (field.isAnnotationPresent(ModularListConfig.class))
                ? field.getAnnotation(ModularListConfig.class).objectKeyName()
                : "type";
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
