package com.eden.orchid.netlifycms.pages

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifycms.util.toNetlifyCmsField
import org.json.JSONArray
import org.json.JSONObject

class NetlifyCmsAdminPage(
        resource: OrchidResource,
        val templateTags: Set<TemplateTag>,
        val components: Set<OrchidComponent>,
        val menuItems: Set<OrchidMenuItem>
) : OrchidPage(resource, "contentManager") {

    public fun getTemplateFieldsFromTag(tag: TemplateTag): JSONArray {
        val fields = JSONArray()

        tag.describeOptions(context).optionsDescriptions.forEach {
            fields.put(it.toNetlifyCmsField())
        }

        if (tag.hasContent()) {
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

    public fun isLocal(): Boolean {
        return context.taskType == TaskService.TaskType.SERVE
    }

    override fun loadAssets() {
        super.loadAssets()

        if(isLocal()) {
            addCss("assets/css/fs-backend.min.css")
            addJs("assets/js/fs-backend.min.js")
        }
    }
}
