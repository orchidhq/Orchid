package com.eden.orchid.swagger.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import org.json.JSONObject

import javax.inject.Inject

class SwaggerComponent @Inject
constructor(context: OrchidContext) : OrchidComponent(context, "swaggerUi", 100) {

    @Option
    lateinit var openApiSource: String

    @Option @StringDefault("swagger-ui")
    lateinit var swaggerElementId: String

    @Option
    lateinit var allSwaggerOptions: JSONObject

    override fun loadAssets() {
        addCss("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui.css")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui-bundle.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/3.5.0/swagger-ui-standalone-preset.js")
    }

}

