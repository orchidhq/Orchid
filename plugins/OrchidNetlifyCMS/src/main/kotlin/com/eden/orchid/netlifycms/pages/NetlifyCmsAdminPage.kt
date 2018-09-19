package com.eden.orchid.netlifycms.pages

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.assets.CssPage
import com.eden.orchid.api.theme.assets.JsPage
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifycms.isRunningLocally
import com.eden.orchid.netlifycms.util.toNetlifyCmsField
import org.json.JSONArray
import org.json.JSONObject

@Description(value = "The page hosting the Netlify CMS.", name = "Netlify CMS")
class NetlifyCmsAdminPage(
        resource: OrchidResource,
        val templateTags: Set<TemplateTag>,
        val components: Set<OrchidComponent>,
        val menuItems: Set<OrchidMenuItem>,
        val useNetlifyIdentityWidget: Boolean
) : OrchidPage(resource, "contentManager", "Orchid Content Manager") {

    public fun getTemplateFieldsFromTag(tag: TemplateTag): JSONArray {
        val fields = JSONArray()

        tag.describeOptions(context).optionsDescriptions.forEach {
            fields.put(it.toNetlifyCmsField())
        }

        if (tag.type == TemplateTag.Type.Content) {
            val filtersField = JSONObject()
            filtersField.put("label", "Content Filters")
            filtersField.put("name", "filters")
            filtersField.put("widget", "string")
            fields.put(filtersField)

            val bodyField = JSONObject()
            bodyField.put("label", "Tag Body")
            bodyField.put("name", "body")
            bodyField.put("widget", "markdown")
            fields.put(bodyField)
        }

        return fields
    }

    public fun getTemplateFieldsFromComponent(tag: OrchidComponent): JSONArray {
        val fields = JSONArray()

        tag.describeOptions(context).optionsDescriptions.forEach {
            fields.put(it.toNetlifyCmsField())
        }

        return fields
    }

    public fun getTemplateFieldsFromMenuItem(tag: OrchidMenuItem): JSONArray {
        val fields = JSONArray()

        tag.describeOptions(context).optionsDescriptions.forEach {
            fields.put(it.toNetlifyCmsField())
        }

        return fields
    }

    public fun getTagProps(tag: TemplateTag): String {
        var json = JSONArray()
        tag.describeOptions(context).optionsDescriptions.forEach { optionsDescription ->
            json.put(optionsDescription.key)
        }
        return json.toString()
    }

    public fun isRunningLocally(): Boolean {
        return isRunningLocally(context)
    }

    override fun loadAssets() {
        super.loadAssets()
        addJs("https://unpkg.com/netlify-cms@^2.0.0/dist/netlify-cms.js")

        if (isRunningLocally()) {
            addCss("assets/css/fs-backend.min.css")
            addJs("assets/js/fs-backend.min.js")
            addJs("inline:fs-cms-registration.js:CMS.registerBackend(\"orchid-server\", FileSystemBackendClass)")
        }
        else if (useNetlifyIdentityWidget) {
            addJs("https://identity.netlify.com/v1/netlify-identity-widget.js")
        }
    }

    // override to prevent theme scripts and styles from being added to this page
    override fun collectThemeScripts(scripts: MutableList<JsPage>) {

    }

    override fun collectThemeStyles(styles: MutableList<CssPage>) {

    }

}
