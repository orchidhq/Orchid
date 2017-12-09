package com.eden.orchid.presentations;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FormField implements OptionsHolder {

    @Getter protected final OrchidContext context;
    @Getter protected String key;

    @Option
    @StringDefault("text")
    @Getter @Setter protected String type;

    @Option @Setter protected String label;

    @Option @Setter protected String placeholder;

    @Option @Setter protected String[] templates;

    @Option @StringDefault("auto") @Setter protected String span;
    @Option @StringDefault("auto") @Setter protected String spanSm;

    @Inject
    public FormField(OrchidContext context) {
        this.context = context;
    }

    public void initialize(String key, JSONObject fieldData) {
        this.key = key;
        extractOptions(context, fieldData);
    }

    public List<String> getTemplates() {
        List<String> allTemplates = new ArrayList<>();
        if(!EdenUtils.isEmpty(templates)) {
            Collections.addAll(allTemplates, templates);
        }

        allTemplates.add("fields/" + key + ".peb");
        allTemplates.add("fields/" + type + ".peb");

        return allTemplates;
    }

    public String getLabel() {
        return (!EdenUtils.isEmpty(label)) ? label : key;
    }

    public String getPlaceholder() {
        return (!EdenUtils.isEmpty(placeholder)) ? placeholder : getLabel();
    }

    public String getSpan() {
        String wrapperClasses = "";

        switch(span) {
            case "right": wrapperClasses += "col-right "; break;
            default: wrapperClasses += "col "; break;
        }

        switch(span) {
            case "full": wrapperClasses += "col-lg-12 "; break;
            case "left": wrapperClasses += "col-lg-6 "; break;
            case "right": wrapperClasses += "col-lg-6 "; break;
            case "auto": wrapperClasses += "col-lg-6 "; break;
            default:
                try {
                    int colSpan = Integer.parseInt(span);
                    wrapperClasses += "col-lg-" + colSpan + " ";
                }
                catch (NumberFormatException e) {
                    wrapperClasses += "col-lg-6 ";
                }
                break;
        }
        switch(spanSm) {
            case "auto": wrapperClasses += "col-sm-12 "; break;
            default:
                try {
                    int colSpan = Integer.parseInt(spanSm);
                    wrapperClasses += "col-sm-" + colSpan + " ";
                }
                catch (NumberFormatException e) {
                    wrapperClasses += "col-sm-6 ";
                }
                break;
        }

        return wrapperClasses;
    }

    public abstract boolean acceptsType(String type);

}
