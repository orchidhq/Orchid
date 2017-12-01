package com.eden.orchid.languages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.components.OrchidComponent;
import org.json.JSONObject;

import javax.inject.Inject;

public final class SwaggerComponent extends OrchidComponent {

    @Option
    public String openApiSource;

    @Option
    @StringDefault("swagger-ui")
    public String swaggerElementId;

    @Option
    public JSONObject allSwaggerOptions;

    protected boolean hasAddedAssets;

    @Inject
    public SwaggerComponent(OrchidContext context) {
        super(context, "swaggerUi", 100);
    }

    @Override
    public void addAssets() {
        if (!hasAddedAssets) {
            super.addAssets();

            addCss("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui.css");
            addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui-bundle.js");
            addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui-standalone-preset.js");
            hasAddedAssets = true;
        }
    }

}
