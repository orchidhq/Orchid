package com.eden.orchid.impl.themes.templateFunctions;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public final class AlertFunction extends TemplateFunction {

    private final OrchidContext context;

    @Option @StringDefault("info")
    @Getter @Setter
    private String level;

    @Inject
    public AlertFunction(OrchidContext context) {
        super("alert");
        this.context = context;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public String[] parameters() {
        return new String[] {"level"};
    }

    @Override
    public Object apply(Object input) {
        OrchidResource resource = context.getResourceEntry("templates/includes/alert.peb");
        if (resource != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("content", input);
            data.put("alertLevel", level);

            return resource.compileContent(data);
        }
        else {
            return input;
        }
    }

}