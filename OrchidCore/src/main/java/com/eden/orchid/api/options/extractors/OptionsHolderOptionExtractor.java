package com.eden.orchid.api.options.extractors;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ListClass;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
 * List of OptionsHolders of the same Class. The class to deserialize by in that List should be set with @ListClass on
 * the field.
 */
public final class OptionsHolderOptionExtractor extends OptionExtractor<OptionsHolder> {

    private final Provider<OptionsExtractor> extractorProvider;
    private final Provider<OrchidContext> contextProvider;

    @Inject
    public OptionsHolderOptionExtractor(Provider<OptionsExtractor> extractorProvider, Provider<OrchidContext> contextProvider) {
        super(25);
        this.extractorProvider = extractorProvider;
        this.contextProvider = contextProvider;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        if(OptionsHolder.class.isAssignableFrom(clazz)) {
            return true;
        }
        if(List.class.isAssignableFrom(clazz)) {
            return true;
        }

        return false;
    }

    @Override
    public OptionsHolder getOption(Field field, JSONObject options, String key) {
        try {
            OptionsHolder holder = (OptionsHolder) contextProvider.get().getInjector().getInstance(field.getType());
            if(options.has(key)) {
                extractorProvider.get().extractOptions(holder, options.getJSONObject(key));
            }
            else {
                extractorProvider.get().extractOptions(holder, new JSONObject());
            }
            return holder;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<OptionsHolder> getList(Field field, JSONObject options, String key) {
        JSONArray array = (options.has(key)) ? options.getJSONArray(key) : new JSONArray();
        List<OptionsHolder> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                OptionsHolder holder = (OptionsHolder) field.getAnnotation(ListClass.class).value().newInstance();
                extractorProvider.get().extractOptions(holder, array.getJSONObject(i));
                list.add(holder);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public Object getArray(Field field, JSONObject options, String key) {
        return this.getList(field, options, key);
    }
}
