package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.converters.FlexibleIterableConverter;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.google.inject.Provider;
import org.json.JSONArray;

import javax.inject.Inject;
import java.lang.reflect.Field;

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
public final class ComponentHolderOptionExtractor extends OptionExtractor<ComponentHolder> {

    private final Provider<OrchidContext> contextProvider;
    private final FlexibleIterableConverter iterableConverter;

    @Inject
    public ComponentHolderOptionExtractor(Provider<OrchidContext> contextProvider, FlexibleIterableConverter iterableConverter) {
        super(100);
        this.contextProvider = contextProvider;
        this.iterableConverter = iterableConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(ComponentHolder.class);
    }

    @Override
    public ComponentHolder getOption(Field field, Object sourceObject, String key) {
        if(sourceObject instanceof JSONArray) {
            return new ComponentHolder(contextProvider.get(), (JSONArray) sourceObject);
        }

        return null;
    }

    @Override
    public ComponentHolder getDefaultValue(Field field) {
        return new ComponentHolder(contextProvider.get(), new JSONArray());
    }

    @Override
    public String describeDefaultValue(Field field) {
        return "Empty ComponentHolder";
    }

}
