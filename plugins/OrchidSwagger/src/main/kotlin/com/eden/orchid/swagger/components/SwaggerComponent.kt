package com.eden.orchid.swagger.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import org.json.JSONObject

@Description("Embed the Swagger UI and point it to your OpenApi.json spec.", name = "Swagger UI")
class SwaggerComponent : OrchidComponent("swaggerUi", 100) {

    @Option
    @StringDefault("3.5.0")
    @Description("The version of SwaggerUI to use.")
    lateinit var swaggerUiVersion: String

    @Option
    @Description("The URL containing the OpenAPI definition.")
    lateinit var openApiSource: String

    @Option
    @StringDefault("swagger-ui")
    @Description("The ID of the element that should contain the Swagger UI.")
    lateinit var swaggerElementId: String

    @Option
    @Description("The full JSON object that initializes the Swagger UI, allowing you to completely customize it.")
    lateinit var allSwaggerOptions: JSONObject

    override fun loadAssets() {
        addCss("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/$swaggerUiVersion/swagger-ui.css")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/$swaggerUiVersion/swagger-ui-bundle.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/swagger-ui/$swaggerUiVersion/swagger-ui-standalone-preset.js")
    }

}

