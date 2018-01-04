package com.eden.orchid.posts.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource

@Archetype(value = ConfigArchetype::class, key = "postTagArchivePages")
class PostTagArchivePage(resource: OrchidResource, index: Int, permalink: String) : PostArchivePage(resource, "postTagArchive", index, permalink) {

    var tag: String
        get() = category!!
        set(tag) {
            this.category = tag
        }

    init {
        this.setTemplates(arrayOf("postTagArchive", "postArchive"))
    }

    override fun getTemplates(): List<String> {
        val templates = super.getTemplates().toMutableList()
        if (!EdenUtils.isEmpty(category)) {
            templates.add(0, "postTagArchive-" + category)
        }

        return templates
    }
}

