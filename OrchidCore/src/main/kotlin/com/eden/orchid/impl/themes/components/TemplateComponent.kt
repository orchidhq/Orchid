package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import org.json.JSONObject

@Description(value = "Display a component with a user-defined template and data.", name = "Template")
class TemplateComponent : OrchidComponent("template", 100) {

    @Option
    @Description("Custom data to be rendered into the custom template.")
    var data: JSONObject? = null

}
