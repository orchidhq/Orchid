package com.eden.orchid.presentations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Form implements OptionsHolder {

    protected final OrchidContext context;

    @Getter protected final String key;
    @Getter protected final JSONObject formData;

    @Option @Getter @Setter protected String title;

    @Option @Getter @Setter protected String action;

    @Option
    @StringDefault("POST")
    @Getter @Setter protected String method;

    @Option @Setter protected JSONObject attributes;

    @Getter @Setter protected Map<String, FormField> fields;

    public Form(OrchidContext context, String key, JSONObject formData) {
        this.context = context;
        this.key = key;
        this.formData = formData;
        this.fields = new HashMap<>();
        try {
            extractOptions(context, formData);

            if (formData.has("fields")) {
                JSONObject formFields = formData.getJSONObject("fields");

                Set<FormField> fieldTypes = context.resolveSet(FormField.class);

                for (String fieldKey : formFields.keySet()) {
                    for (FormField fieldType : fieldTypes) {
                        JSONObject fieldConfig = formFields.getJSONObject(fieldKey);
                        if (fieldType.acceptsType(fieldConfig.getString("type"))) {
                            FormField formField = context.getInjector().getInstance(fieldType.getClass());
                            formField.initialize(fieldKey, fieldConfig);
                            fields.put(fieldKey, formField);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAttributes() {
        String attrs = "";

        for (String key : attributes.keySet()) {
            attrs += key + "=\"" + attributes.get(key).toString() + "\" ";
        }

        return attrs;
    }
}
