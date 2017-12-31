package com.eden.orchid.presentations;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.converters.StringConverter;
import com.eden.orchid.api.options.OptionExtractor;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

public final class FormOptionExtractor extends OptionExtractor<Form> {

    private final Provider<OrchidContext> contextProvider;
    private final StringConverter converter;
    private final FormsModel formsModel;

    @Inject
    public FormOptionExtractor(Provider<OrchidContext> contextProvider, StringConverter converter, FormsModel formsModel) {
        super(1000);
        this.contextProvider = contextProvider;
        this.converter = converter;
        this.formsModel = formsModel;
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(Form.class);
    }

    @Override
    public Form getOption(Field field, JSONObject options, String key) {
        if(options.has(key)) {
            if(options.get(key) instanceof JSONObject) {
                return new Form(contextProvider.get(), key, options.getJSONObject(key));
            }
            else {
                EdenPair<Boolean, String> value = converter.convert(options.get(key));
                if(value.first) {
                    return formsModel.getForms().getOrDefault(value.second, null);
                }
            }
        }

        return getDefaultValue(field);
    }

    @Override
    public Form getDefaultValue(Field field) {
        return null;
    }

    @Override
    public List<Form> getList(Field field, JSONObject options, String key) {
        return null;
    }

    @Override
    public Object[] getArray(Field field, JSONObject options, String key) {
        return null;
    }
}
