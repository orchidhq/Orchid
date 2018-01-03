package com.eden.orchid.posts.pages

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.options.annotations.ApplyBaseUrl
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.utils.PostsExcerptStrategy
import java.text.SimpleDateFormat
import java.util.*

class PostPage(resource: OrchidResource, var postsModel: PostsModel) : PermalinkPage(resource, "post") {

    var year: Int = 0
    var month: Int = 0
    var day: Int = 0

    var category: String? = null

    @Option
    var author: Author? = null

    @Option
    lateinit var tags: Array<String>

    @Option @ApplyBaseUrl
    lateinit var featuredImage: String

    @Option
    lateinit var tagline: String

    @Option("permalink")
    lateinit var postPermalink: String

    override fun getPermalink(): String {
        return postPermalink
    }

    val excerpt: String
        get() {
            val strategy = context.injector.getInstance(PostsExcerptStrategy::class.java)
            return strategy.getExcerpt(this)
        }

    val categoryPage: PostArchivePage?
        get() = postsModel.getCategoryLandingPage(this.category)

    val tagPages: List<PostTagArchivePage>
        get() {
            val tagArchivePages = ArrayList<PostTagArchivePage>()

            for (tag in tags) {
                val tagArchivePage = getTagPage(tag)
                if (tagArchivePage != null) {
                    tagArchivePages.add(tagArchivePage)
                }
            }

            return tagArchivePages
        }

    fun publishedDate(format: String = "MMMMM d, yyyy"): String {
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, year)
        date.set(Calendar.MONTH, month - 1)
        date.set(Calendar.DAY_OF_MONTH, day)

        return SimpleDateFormat(format).format(date.time)
    }

    fun getTagPage(tag: String): PostTagArchivePage? {
        return postsModel.getTagLandingPage(tag)
    }

    override fun getTemplates(): List<String> {
        val templates = super.getTemplates()
        if (!EdenUtils.isEmpty(category)) {
            templates.add(0, category)
            templates.add(0, "post-" + category!!)
        }

        return templates
    }

}

