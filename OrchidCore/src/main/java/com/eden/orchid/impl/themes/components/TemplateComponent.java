package com.eden.orchid.impl.themes.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.inject.Inject;

@Description(value = "Display a component with a user-defined template and data.", name = "Template")
public final class TemplateComponent extends OrchidComponent {

    @Getter
    @Setter
    @Option
    @Description("Custom data to be rendered into the custom template.")
    public JSONObject data;

    @Inject
    public TemplateComponent(OrchidContext context) {
        super(context, "template", 100);
    }

}
