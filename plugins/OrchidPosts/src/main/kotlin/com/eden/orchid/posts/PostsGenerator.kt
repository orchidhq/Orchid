package com.eden.orchid.posts

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.FolderCollection
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.ListClass
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.api.theme.permalinks.PermalinkStrategy
import com.eden.orchid.posts.model.Author
import com.eden.orchid.posts.model.CategoryModel
import com.eden.orchid.posts.model.PostsModel
import com.eden.orchid.posts.pages.AuthorPage
import com.eden.orchid.posts.pages.PostPage
import com.eden.orchid.posts.utils.PostsUtils
import com.eden.orchid.posts.utils.isToday
import com.eden.orchid.utilities.dashCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words
import java.time.LocalDate
import java.util.regex.Pattern
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Share your thoughts and interests with blog posts and archives.")
class PostsGenerator @Inject
constructor(context: OrchidContext, val permalinkStrategy: PermalinkStrategy, val postsModel: PostsModel)
    : OrchidGenerator(context, "posts", OrchidGenerator.PRIORITY_EARLY) {

    companion object {
        val pageTitleRegex = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})-([\\w-]+)")
    }

    @Option
    @StringDefault(":category/:year/:month/:day/:slug")
    lateinit var permalink: String

    @Option
    @StringDefault("<!--more-->")
    lateinit var excerptSeparator: String

    @Option
    @ListClass(Author::class)
    var authors: List<Author> = emptyList()

    @Option
    var categories: Array<String> = emptyArray()

    @Option
    @StringDefault("posts")
    lateinit var baseDir: String

    @Option
    @StringDefault("posts/authors")
    lateinit var authorsBaseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val authorPages = getAuthorPages()

        postsModel.initialize(permalink, layout, excerptSeparator, authorPages)

        if (EdenUtils.isEmpty(categories)) {
            val posts = getPostsPages(null)
            postsModel.categories.put(null, CategoryModel(null, posts))
        } else {
            for (category in categories) {
                val posts = getPostsPages(category)
                postsModel.categories.put(category, CategoryModel(category, posts))
            }
        }

        val allPages = ArrayList<OrchidPage>()
        allPages.addAll(authorPages)
        for (key in postsModel.categories.keys) {
            allPages.addAll(postsModel.categories[key]!!.first)
        }

        return allPages
    }

    override fun startGeneration(posts: Stream<out OrchidPage>) {
        posts.forEach({ context.renderTemplate(it) })
    }

    private fun getAuthorPages(): List<AuthorPage> {
        val resourcesList = context.getLocalResourceEntries(authorsBaseDir, null, false)

        val authorPages = ArrayList<AuthorPage>()

        for (entry in resourcesList) {
            val authorPage = AuthorPage(entry, Author(), postsModel)
            authorPage.initializeAuthorFromPageData()
            authorPage.author.authorPage = authorPage
            permalinkStrategy.applyPermalink(authorPage, authorPage.permalink)
            authorPages.add(authorPage)
        }

        for (author in this.authors) {
            val authorPage = AuthorPage(StringResource(context, "index.md", ""), author, postsModel)
            authorPage.author.authorPage = authorPage
            permalinkStrategy.applyPermalink(authorPage, authorPage.permalink)
            authorPages.add(authorPage)
        }

        return authorPages
    }

    private fun getPostsPages(category: String?): MutableList<PostPage> {
        val baseCategoryPath = if (EdenUtils.isEmpty(category)) baseDir else baseDir + "/" + category
        val resourcesList = context.getLocalResourceEntries(baseCategoryPath, null, true)

        val posts = ArrayList<PostPage>()

        for (entry in resourcesList) {
            val formattedFilename = PostsUtils.getPostFilename(entry, baseCategoryPath)
            val matcher = pageTitleRegex.matcher(formattedFilename)

            if (matcher.matches()) {
                val post = PostPage(entry, postsModel, category)

                post.title = matcher.group(4).from { dashCase() }.to { words { capitalize() } }

                if(post.publishDate.isToday()) {
                    post.publishDate = LocalDate.of(
                            Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3))
                    )
                }

                val permalink = if (!EdenUtils.isEmpty(post.permalink)) post.permalink else postsModel.permalink
                permalinkStrategy.applyPermalink(post, permalink)
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

    override fun getCollections(): List<OrchidCollection<*>> {
        val collectionsList = ArrayList<OrchidCollection<*>>()

        categories.forEach {
            val baseCategoryPath = if (EdenUtils.isEmpty(it)) baseDir else baseDir + "/" + it

            val collection = FolderCollection(
                    this,
                    it,
                    postsModel.categories[it]?.first,
                    PostPage::class.java,
                    baseCategoryPath
            )
            collection.label = "Blog - $it"

            collectionsList.add(collection)
        }

        return collectionsList
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
}

