package com.eden.orchid.posts.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.ApplyBaseUrl
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.PostCategoryArchetype
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.utils.PostsExcerptStrategy

@Archetypes(
    Archetype(value = ConfigArchetype::class, key = "postPages"),
    Archetype(value = PostCategoryArchetype::class, key = "postPages")
)
class PostPage(resource: OrchidResource, var postsModel: PostsModel, val categoryModel: CategoryModel, title: String)
    : OrchidPage(resource, "post", title) {

    @Option
    var author: Author? = null

    @Option
    lateinit var tags: Array<String>

    @Option @ApplyBaseUrl
    lateinit var featuredImage: String

    @Option
    lateinit var tagline: String

    @Option
    lateinit var permalink: String

    val excerpt: String
        get() {
            val strategy = context.injector.getInstance(PostsExcerptStrategy::class.java)
            return strategy.getExcerpt(this)
        }

    val category: String?
        get() {
            return categoryModel.key
        }

    val categories: Array<String>
        get() {
            return categoryModel.path.split("/").toTypedArray()
        }

    val year: Int         get() { return publishDate.year }
    val month: Int        get() { return publishDate.monthValue }
    val monthName: String get() { return publishDate.month.toString() }
    val day: Int          get() { return publishDate.dayOfMonth }

    init {
        this.extractOptions(this.context, data)
        postInitialize(title)
    }

    override fun initialize(title: String?) {

    }

    override fun getTemplates(): List<String> {
        val templates = super.getTemplates()
        if (!EdenUtils.isEmpty(categoryModel.key)) {
            templates.add(0, categoryModel.key)
            templates.add(0, "post-" + categoryModel.key!!)
        }

        return templates
    }

}

