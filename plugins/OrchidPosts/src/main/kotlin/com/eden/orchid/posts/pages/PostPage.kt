package com.eden.orchid.posts.pages

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.posts.PostCategoryArchetype
import com.eden.orchid.posts.PostsGenerator
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.utilities.resolve
import org.json.JSONObject

@Archetypes(
    Archetype(value = PostCategoryArchetype::class, key = PostsGenerator.GENERATOR_KEY),
    Archetype(value = ConfigArchetype::class, key = "${PostsGenerator.GENERATOR_KEY}.postPages")
)
@Description(value = "A blog post.", name = "Blog Post")
class PostPage(
    resource: OrchidResource,
    val categoryModel: CategoryModel,
    title: String
) : OrchidPage(resource, "post", title) {

    @Option("author")
    @Description("The posts author. May be the `name` of a known author, or an anonymous Author config, only " +
            "used for this post, which is considered as a guest author."
    )
    var authorName: Any? = null

    fun setAuthor(authorName: Any?) {
        this.authorName = authorName
    }

    val author: Author? by lazy {
        val postsModel: PostsModel = context.resolve()

        if (authorName is JSONObject) {
            val author = Author()
            author.extractOptions(context, (authorName as JSONObject).toMap())
            author
        }
        else {
            val value = "" + authorName?.toString()
            postsModel.getAuthorByName(value)
        }
    }

    @Option
    @Description("A list of tags for this post, for basic taxonomic purposes. More complex taxonomic relationships " +
            "may be managed by other plugins, which may take post tags into consideration."
    )
    lateinit var tags: Array<String>

    @Option
    @Description("A 'type' of post, such as 'gallery', 'video', or 'blog', which is used to determine the specific" +
            "post template to use for the Page Content."
    )
    lateinit var postType: String

    @Option
    @Description("A fully-specified URL to a post's featured image, or a relative path to an Orchid image.")
    lateinit var featuredImage: AssetRelation

    @Option
    @Description("The permalink structure to use only for this blog post. This overrides the permalink structure set " +
            "in the category configuration."
    )
    lateinit var permalink: String

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
        val templates = ArrayList<String>()
        if(!EdenUtils.isEmpty(postType)) {
            templates.add(0, "$key-type-$postType")
        }
        if(!EdenUtils.isEmpty(categoryModel.key)) {
            templates.add(0, "$key-${categoryModel.key!!}")
        }

        return templates
    }

}

