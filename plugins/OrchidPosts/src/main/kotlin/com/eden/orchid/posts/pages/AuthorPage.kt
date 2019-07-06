package com.eden.orchid.posts.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.PostsGenerator
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.utilities.OrchidUtils

@Archetypes(
        Archetype(value = ConfigArchetype::class, key = "${PostsGenerator.GENERATOR_KEY}.allPages"),
        Archetype(value = ConfigArchetype::class, key = "${PostsGenerator.GENERATOR_KEY}.authorPages")
)
@Description(value = "An 'about' page for an author in your blog.", name = "Author")
class AuthorPage(
        resource: OrchidResource,
        val author: Author
) : OrchidPage(resource, "postAuthor", author.name) {

    @Option
    @StringDefault("authors/:authorName")
    @Description("The permalink structure to use only for this author bio page.")
    lateinit var permalink: String

    lateinit var postsModel: PostsModel

    override fun getTemplates(): List<String> {
        return listOf("$key-${OrchidUtils.toSlug(author.name)}")
    }

}

