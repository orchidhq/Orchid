package com.eden.orchid.posts.model

import com.eden.common.util.EdenUtils
import com.eden.orchid.posts.pages.PostArchivePage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.pages.PostTagArchivePage
import lombok.Getter
import lombok.Setter
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Getter
@Setter
@Singleton
class PostsModel @Inject
constructor() {

    lateinit var permalink: String
    lateinit var layout: String
    lateinit var excerptSeparator: String
    lateinit var authors: List<Author>

    var categories: MutableMap<String?, CategoryModel>
    var tags: MutableMap<String, TagModel>

    val categoryNames: Set<String?>
        get() = categories.keys

    val tagNames: Set<String>
        get() = tags.keys

    init {
        this.categories = LinkedHashMap()
        this.tags = LinkedHashMap()
    }

    fun initialize(permalink: String, layout: String, excerptSeparator: String, authors: List<Author>) {
        this.categories = LinkedHashMap()
        this.tags = LinkedHashMap()

        this.permalink = permalink
        this.layout = layout
        this.excerptSeparator = excerptSeparator
        this.authors = authors
    }

    fun getAuthorByName(authorName: String): Author? {
        if (!EdenUtils.isEmpty(authors)) {
            for (author in authors) {
                if (author.name.equals(authorName, ignoreCase = true)) {
                    return author
                }
            }
        }

        return null
    }

    fun tagPost(tag: String, post: PostPage) {
        tags.putIfAbsent(tag, TagModel(tag))
        tags[tag]!!.first.add(post)
    }

    fun getPostsTagged(tag: String): List<PostPage> {
        return tags[tag]?.first ?: emptyList()
    }

    fun getRecentPosts(category: String?, limitArg: Int): List<PostPage> {
        var limit = limitArg
        var chosenCategory: MutableList<PostPage> = ArrayList()

        if (category.equals(":any", ignoreCase = true)) {
            chosenCategory = ArrayList()

            for (categoryPosts in categories.values) {
                chosenCategory.addAll(categoryPosts.first)
            }
        } else {
            if (categories.containsKey(category)) {
                chosenCategory.addAll(categories[category]!!.first)
            }
        }

        chosenCategory.sortWith(PostsModel.postPageComparator)

        val postPages = ArrayList<PostPage>()

        if (limit >= chosenCategory.size) {
            limit = chosenCategory.size
        }

        for (i in 0 until limit) {
            postPages.add(chosenCategory[i])
        }

        return postPages
    }

    fun getCategoryLandingPage(category: String?): PostArchivePage? {
        return if (categories.containsKey(category)) {
            categories[category]!!.second[0]
        } else null
    }

    fun getTagLandingPage(tag: String): PostTagArchivePage? {
        return if (tags.containsKey(tag)) {
            tags[tag]!!.second[0]
        } else null
    }

    companion object {
        var postPageComparator = kotlin.Comparator<PostPage> { o1, o2 ->
            val criteria = arrayOf("year", "month", "day")
            var result = 0

            for (item in criteria) {
                result = 0

                when (item) {
                    "year" -> result = o2.year - o1.year
                    "month" -> result = o2.month - o1.month
                    "day" -> result = o2.day - o1.day
                }

                if (result != 0) {
                    break
                }
            }

            result
        }
    }
}

