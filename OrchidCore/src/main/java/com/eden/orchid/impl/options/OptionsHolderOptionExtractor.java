package com.eden.orchid.impl.options;

import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.options.OptionsHolder;
import com.google.inject.Provider;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OptionsHolderOptionExtractor implements OptionExtractor<OptionsHolder> {

    private Provider<OptionsExtractor> extractorProvider;

    @Inject
    public OptionsHolderOptionExtractor(Provider<OptionsExtractor> extractorProvider) {
        this.extractorProvider = extractorProvider;
    }

    @Override
    public boolean acceptsClass(Class<?> clazz) {
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
            OptionsHolder holder = (OptionsHolder) field.getType().newInstance();
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
