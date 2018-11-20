package com.eden.orchid.impl.themes.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.components.OrchidComponent
import org.json.JSONObject
import javax.inject.Inject

@Description(value = "Display a component with a user-defined template and data.", name = "Template")
class TemplateComponent
@Inject
constructor(
        context: OrchidContext
) : OrchidComponent(context, "template", 100) {

    @Option
    @Description("Custom data to be rendered into the custom template.")
    var data: JSONObject? = null

}
