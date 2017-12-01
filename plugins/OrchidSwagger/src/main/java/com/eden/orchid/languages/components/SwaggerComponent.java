package com.eden.orchid.languages.components;

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

    @Inject
    public SwaggerComponent(OrchidContext context) {
        super(context, "swaggerUi", 100);
    }

    @Override
    public void addAssets() {
        super.addAssets();

        addCss("assets/css/swagger-ui.css");
        addJs("assets/js/swagger-ui-bundle.js");
        addJs("assets/js/swagger-ui-standalone-preset.js");
    }

}
