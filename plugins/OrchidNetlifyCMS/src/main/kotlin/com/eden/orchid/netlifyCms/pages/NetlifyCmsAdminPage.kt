package com.eden.orchid.netlifyCms.pages

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.netlifyCms.util.toNetlifyCmsField
import org.json.JSONArray
import org.json.JSONObject

class NetlifyCmsAdminPage(
        resource: OrchidResource,
        val templateTags: Set<TemplateTag>
) : OrchidPage(resource, "contentManager") {

    public fun getTemplateFields(tag: TemplateTag): JSONArray {
        val fields = JSONArray()

        tag.describeOptions(context).forEach {
            fields.put(it.toNetlifyCmsField())
        }

        if (tag.hasContent()) {
            val bodyField = JSONObject()
            bodyField.put("label", "Tag Body")
            bodyField.put("name", "body")
            bodyField.put("widget", "markdown")
            fields.put(bodyField)
        }

        return fields
    }

    public fun getTagPattern(tag: TemplateTag): String {
        var pattern = ""

        var params = ""

        tag.describeOptions(context).forEach {
            params += "${it.key}=\"(.*)\" "
        }

        if(!tag.hasContent()) {
            pattern += "{% ${tag.name} $params %}"
        }
        else {
            pattern += "{% ${tag.name} $params:: md %}(.*){% end${tag.name} %}"
        }

        return pattern
    }

    public fun parseTagPattern(tag: TemplateTag): String {
        var data = JSONObject()

        tag.describeOptions(context).forEachIndexed { index, optionsDescription ->
            data.put(optionsDescription.key, "match[${index+1}]")
        }

        if(tag.hasContent()) {
            data.put("body", "match[${tag.describeOptions(context).size+1}]")
        }

        return data.toString().replace(Regex("\"match\\[(\\d+)]\""), "match[$1]")
    }

    public fun getTagBlock(tag: TemplateTag): String {
        var pattern = ""

        var params = ""

        tag.describeOptions(context).forEach { optionsDescription ->
            params += "${optionsDescription.key}=\"\${obj.${optionsDescription.key}}\" "
        }

        if(!tag.hasContent()) {
            pattern += "{% ${tag.name} $params %}"
        }
        else {
            pattern += "{% ${tag.name} $params:: md %}\${obj.body}{% end${tag.name} %}"
        }

        return pattern
    }

    public fun getTagPreview(tag: TemplateTag): String {
        return getTagBlock(tag)
    }

}
