package com.eden.orchid.presentations.fields;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.presentations.FormField;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public class TextField extends FormField {

    @Inject
    public TextField(OrchidContext context) {
        super(context);
    }

    public void initialize(String key, JSONObject fieldData) {
        super.initialize(key, fieldData);
    }

    public List<String> getTemplates() {
        List<String> allTemplates = super.getTemplates();
        allTemplates.add("fields/textual.twig");
        return allTemplates;
    }

    public boolean acceptsType(String type) {
        switch(type) {
            case "text":
            case "number":
                return true;
            default:
                return false;
        }
    }

}
