package com.eden.orchid.impl.themes.functions

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.converters.StringConverter
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource

import javax.inject.Inject

@Description(value = "Load a resource's content into a String.", name = "Load")
class LoadFunction @Inject
constructor(private val context: OrchidContext, private val converter: StringConverter) :
    TemplateFunction("load", true) {

    @Option
    @Description("The resource to lookup and render in-place.")
    var resource: String? = null

    @Option
    @BooleanDefault(true)
    @Description("When true, only resources from local sources are considered, otherwise resources from all plugins "
            + "and from the current theme will also be considered.")
    var isLocalResourcesOnly: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("resource", "localResourcesOnly")
    }

    override fun apply(): Any {
        val foundResource =
            if (isLocalResourcesOnly) context.getLocalResourceEntry(resource) else context.getResourceEntry(resource)

        return if (foundResource != null) {
            foundResource.compileContent(null)
        } else ""

    }
}