package com.eden.orchid.posts

import com.caseyjbrooks.clog.Clog
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ListClass
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.model.PostsPaginator
import com.eden.orchid.posts.pages.PostArchivePage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.pages.PostTagArchivePage
import com.eden.orchid.posts.permalink.PostsPermalinkStrategy
import com.eden.orchid.posts.utils.PostsUtils
import com.eden.orchid.utilities.dashCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words
import org.apache.commons.lang3.StringUtils
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Share your thoughts and interests with blog posts and archives.")
class PostsGenerator @Inject
constructor(context: OrchidContext, val permalinkStrategy: PostsPermalinkStrategy, val postsModel: PostsModel)
    : OrchidGenerator(context, "posts", 700) {

    @Option
    @StringDefault(":category/:year/:month/:day/:slug")
    lateinit var permalink: String

    @Option
    @StringDefault(":category/archive/:archiveIndex")
    lateinit var archivePermalink: String

    @Option
    @StringDefault("tags/:tag/:archiveIndex")
    lateinit var tagArchivePermalink: String

    @Option
    @StringDefault("<!--more-->")
    lateinit var excerptSeparator: String

    @Option
    @ListClass(Author::class)
    var authors: List<Author> = emptyList()

    @Option
    var categories: Array<String> = emptyArray()

    @Option
    lateinit var pagination: PostsPaginator

    @Option
    lateinit var categoryPagination: JSONObject

    @Option
    @StringDefault("posts")
    lateinit var baseDir: String

    override fun startIndexing(): List<OrchidPage> {
        postsModel.initialize(permalink, layout, excerptSeparator, authors)

        if (EdenUtils.isEmpty(categories)) {
            val posts = getPostsList(null)
            val archive = buildArchive(null, posts, pagination)
            postsModel.categories.put(null, CategoryModel(null, posts, archive))
            tagPosts(posts)
        } else {
            for (category in categories) {
                val posts = getPostsList(category)

                var categoryPaginator: PostsPaginator = pagination

                if(categoryPagination.has(category) && categoryPagination.get(category) is JSONObject) {
                    categoryPaginator = PostsPaginator()
                    categoryPaginator.extractOptions(context, categoryPagination.getJSONObject(category))
                }

                val archive = buildArchive(category, posts, categoryPaginator)
                postsModel.categories.put(category, CategoryModel(category, posts, archive))
                tagPosts(posts)
            }
        }

        for (tag in postsModel.tagNames) {
            val posts = postsModel.getPostsTagged(tag)
            val archive = buildTagArchive(tag, posts, pagination)
            postsModel.tags[tag]!!.second.addAll(archive)
        }

        val allPages = ArrayList<OrchidPage>()
        for (key in postsModel.categories.keys) {
            allPages.addAll(postsModel.categories[key]!!.first)
            allPages.addAll(postsModel.categories[key]!!.second)
        }
        for (key in postsModel.tags.keys) {
            allPages.addAll(postsModel.tags[key]!!.second)
        }

        return allPages
    }

    override fun startGeneration(posts: Stream<out OrchidPage>) {
        posts.forEach({ context.renderTemplate(it) })
    }

    private fun previous(posts: List<OrchidPage>, i: Int): OrchidPage? {
        if (posts.size > 1) {
            if (i != 0) {
                return posts[i - 1]
            }
        }

        return null
    }

    private fun next(posts: List<OrchidPage>, i: Int): OrchidPage? {
        if (posts.size > 1) {
            if (i < posts.size - 1) {
                return posts[i + 1]
            }
        }

        return null
    }

    private fun getPostsList(category: String?): MutableList<PostPage> {
        val baseCategoryPath = if (EdenUtils.isEmpty(category)) baseDir else baseDir + "/" + category
        val resourcesList = context.getLocalResourceEntries(baseCategoryPath, null, true)

        val posts = ArrayList<PostPage>()

        for (entry in resourcesList) {
            val formattedFilename = PostsUtils.getPostFilename(entry, baseCategoryPath)
            val matcher = pageTitleRegex.matcher(formattedFilename)

            if (matcher.matches()) {
                val post = PostPage(entry, postsModel)

                post.year = Integer.parseInt(matcher.group(1))
                post.month = Integer.parseInt(matcher.group(2))
                post.day = Integer.parseInt(matcher.group(3))
                post.title = matcher.group(4).from { dashCase() }.to { words { capitalize() } }
                post.category = category

                permalinkStrategy.applyPermalink(post)
                posts.add(post)
            }
        }

        posts.sortWith(PostsModel.postPageComparator)
        posts.mapIndexed { i, post ->
            if (next(posts, i) != null) {
                post.previous = next(posts, i)
            }
            if (previous(posts, i) != null) {
                post.next = previous(posts, i)
            }
        }

        return posts
    }

    private fun buildArchive(category: String?, posts: List<PostPage>, paginator: PostsPaginator): MutableList<PostArchivePage> {
        val archivePages = ArrayList<PostArchivePage>()

        val pages = Math.ceil((posts.size / paginator.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val postPageList = posts.subList(i * paginator.pageSize, Math.min((i + 1) * paginator.pageSize, posts.size))

            if (postPageList.isNotEmpty()) {
                val pageRef = OrchidReference(context, "archive.html")

                if (i == 0) {
                    if (!EdenUtils.isEmpty(category)) {
                        pageRef.title = StringUtils.capitalize(category)
                    } else {
                        pageRef.title = "Blog Archive"
                    }
                } else {
                    if (!EdenUtils.isEmpty(category)) {
                        pageRef.title = StringUtils.capitalize(category) + " Archive (Page " + (i + 1) + ")"
                    } else {
                        pageRef.title = "Blog Archive (Page " + (i + 1) + ")"
                    }
                }

                val permalink = if (!EdenUtils.isEmpty(paginator.permalink)) paginator.permalink else archivePermalink

                val page = PostArchivePage(StringResource("", pageRef), i + 1, permalink)
                page.postList = postPageList

                if (!EdenUtils.isEmpty(category)) {
                    page.category = category
                }

                permalinkStrategy.applyPermalink(page)

                archivePages.add(page)
            }
        }

        archivePages.mapIndexed { i, postArchive ->
            if (next(archivePages, i) != null) {
                postArchive.next = next(archivePages, i)
            }
            if (previous(archivePages, i) != null) {
                postArchive.previous = previous(archivePages, i)
            }
        }

        return archivePages
    }

    private fun buildTagArchive(tag: String, posts: List<PostPage>, paginator: PostsPaginator): List<PostTagArchivePage> {
        val tagArchivePages = ArrayList<PostTagArchivePage>()

        val pages = Math.ceil((posts.size / paginator.pageSize).toDouble()).toInt()

        for (i in 0..pages) {
            val postPageList = posts.subList(i * paginator.pageSize, Math.min((i + 1) * paginator.pageSize, posts.size))
            if (postPageList.size > 0) {
                val pageRef = OrchidReference(context, "tag.html")

                if (i == 0) {
                    pageRef.title = Clog.format("Posts tagged '#{$1}'", StringUtils.capitalize(tag))
                } else {
                    pageRef.title = Clog.format("Posts tagged '#{$1}' (Page #{$2})", StringUtils.capitalize(tag), i + 1)
                }

                val permalink = if (!EdenUtils.isEmpty(paginator.permalink)) paginator.permalink else tagArchivePermalink

                val page = PostTagArchivePage(StringResource("", pageRef), i + 1, permalink)
                page.postList = postPageList
                page.tag = tag

                permalinkStrategy.applyPermalink(page)

                tagArchivePages.add(page)
            }
        }

        var i = 0
        for (post in tagArchivePages) {
            if (next(posts, i) != null) {
                post.next = next(tagArchivePages, i)
            }
            if (previous(posts, i) != null) {
                post.previous = previous(tagArchivePages, i)
            }
            i++
        }

        return tagArchivePages
    }

    private fun tagPosts(posts: List<PostPage>) {
        for (post in posts) {
            if (!EdenUtils.isEmpty(post.tags)) {
                for (tag in post.tags) {
                    postsModel.tagPost(tag, post)
                }
            }
        }
    }

    companion object {
        val pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)")
    }
}

