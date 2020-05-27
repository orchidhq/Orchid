@file:Suppress(SuppressedWarnings.DEPRECATION)
package com.eden.orchid.javadoc.pages

import com.copperleaf.javadoc.json.models.JavaDocElement
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.javadoc.JavadocGenerator
import com.eden.orchid.javadoc.resources.BaseJavadocResource
import com.eden.orchid.utilities.SuppressedWarnings

@Archetype(value = ConfigArchetype::class, key = "${JavadocGenerator.GENERATOR_KEY}.pages")
abstract class BaseJavadocPage(
    resource: BaseJavadocResource,
    key: String,
    title: String
) : OrchidPage(resource, RenderService.RenderMode.TEMPLATE, key, title) {

    fun renderCommentText(el: JavaDocElement) : String {
        val builder = StringBuilder()

        for(richTextComponent in el.comment) {
            if(richTextComponent.kind in listOf("see", "link")) {
                val linkedPage = context.findPage(null, null, richTextComponent.className)
                if(linkedPage != null) {
                    builder.append(""" <a href="${linkedPage.link}">${linkedPage.title}</a> """)
                }
            }
            else {
                builder.append(richTextComponent.text)
            }
        }

        return builder.toString()
    }

}
