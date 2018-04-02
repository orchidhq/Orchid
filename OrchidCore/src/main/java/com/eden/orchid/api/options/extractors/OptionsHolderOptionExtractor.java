package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.converters.FlexibleMapConverter;
import com.google.inject.Provider;
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
 * | JSONArray  | direct   |
 * | anything[] | new JSONArray from array |
 * | List       | new JSONArray from list |
 *
 *
 * ### Destination Types
 *
 * | Field Type                    | Annotation   | Default Value            |
 * |-------------------------------|--------------|--------------------------|
 * | ? extends OptionsHolder       | none         | null                     |
 * | List<? extends OptionsHolder> | none         | null                     |
 *
 * ### _Notes_
 *
 * This can deserialize any JSONObject into any class that implements OptionsHolder, and can also handle any generic
 * List of OptionsHolders of the same Class.
 *
 * @since v1.0.0
 * @orchidApi optionTypes
 */
public final class OptionsHolderOptionExtractor extends OptionExtractor<OptionsHolder> {

    private final Provider<OptionsExtractor> extractorProvider;
    private final Provider<OrchidContext> contextProvider;
    private final FlexibleMapConverter mapConverter;

    @Inject
    public OptionsHolderOptionExtractor(Provider<OptionsExtractor> extractorProvider, Provider<OrchidContext> contextProvider, FlexibleMapConverter mapConverter) {
        super(25);
        this.extractorProvider = extractorProvider;
        this.contextProvider = contextProvider;
        this.mapConverter = mapConverter;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return OptionsHolder.class.isAssignableFrom(clazz);
    }

    @Override
    public OptionsHolder getOption(Field field, Object sourceObject, String key) {
//        try {
//            OptionsHolder holder = (OptionsHolder) contextProvider.get().getInjector().getInstance(field.getType());
//            JSONObject sourceJson = new JSONObject(mapConverter.convert(sourceObject).second);
//            extractorProvider.get().extractOptions(holder, sourceJson);
//
//            return holder;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        if(sourceObject instanceof JSONObject) {
            OptionsHolder holder = (OptionsHolder) contextProvider.get().getInjector().getInstance(field.getType());
            extractorProvider.get().extractOptions(holder, (JSONObject) sourceObject);

            return holder;
        }

        return null;
    }

    @Override
    public OptionsHolder getDefaultValue(Field field) {
        return null;
    }

}
