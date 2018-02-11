package com.eden.orchid.posts

import com.caseyjbrooks.clog.Clog
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
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.dashCase
import com.eden.orchid.utilities.from
import com.eden.orchid.utilities.to
import com.eden.orchid.utilities.words
import org.json.JSONArray
import org.json.JSONObject
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
    @StringDefault("<!--more-->")
    lateinit var excerptSeparator: String

    @Option
    @ListClass(Author::class)
    var authors: List<Author> = emptyList()

    @Option
    lateinit var categories: JSONArray

    @Option
    @StringDefault("posts")
    lateinit var baseDir: String

    @Option
    @StringDefault("posts/authors")
    lateinit var authorsBaseDir: String

    @Option
    lateinit var config: JSONObject

    override fun startIndexing(): List<OrchidPage> {
        val authorPages = getAuthorPages()

        postsModel.initialize(excerptSeparator)

        if (categories.length() > 0) {
            for (i in 0 until categories.length()) {
                val category = categories.get(i)
                val categoryKey: String
                val categoryOptions: JSONObject?

                if (category is String) {
                    categoryKey = category
                    categoryOptions = null
                } else if (category is JSONObject) {
                    if (category.length() == 1) {
                        categoryKey = category.keySet().first()
                        categoryOptions = category.get(categoryKey) as? JSONObject
                    } else {
                        categoryKey = category.getString("key")
                        categoryOptions = category
                    }
                } else {
                    continue
                }

                val categoryModel = postsModel.getCategory(OrchidUtils.normalizePath(categoryKey), categoryOptions ?: JSONObject())
            }
        }
        else {
            val categoryModel = postsModel.getCategory(null, config)
        }

        val allPages = ArrayList<OrchidPage>()

        if(postsModel.validateCategories()) {
            allPages.addAll(authorPages)
            postsModel.categories.values.forEach { categoryModel ->
                categoryModel.first = getPostsPages(categoryModel)
                allPages.addAll(categoryModel.first)
            }
        }
        else {
            Clog.e("Categories are not hierarchical, cannot continue generating posts.")
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

    private fun getPostsPages(categoryModel: CategoryModel): MutableList<PostPage> {
        val baseCategoryPath = OrchidUtils.normalizePath(baseDir + "/" + categoryModel.path)
        val resourcesList = context.getLocalResourceEntries(baseCategoryPath, null, true)

        val posts = ArrayList<PostPage>()

        for (entry in resourcesList) {
            val formattedFilename = PostsUtils.getPostFilename(entry, baseCategoryPath)
            val matcher = pageTitleRegex.matcher(formattedFilename)

            if (matcher.matches()) {
                val title = matcher.group(4).from { dashCase() }.to { words { capitalize() } }
                val post = PostPage(entry, postsModel, categoryModel, title)

                if(post.publishDate.isToday()) {
                    post.publishDate = LocalDate.of(
                            Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(2)),
                            Integer.parseInt(matcher.group(3))
                    )
                }

                val permalink = if (!EdenUtils.isEmpty(post.permalink)) post.permalink else categoryModel.permalink
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

        postsModel.categories.values.forEach {
            val baseCategoryPath = if (EdenUtils.isEmpty(it.key)) baseDir else baseDir + "/" + it

            val collection = FolderCollection(
                    this,
                    it.key,
                    it.first as List<OrchidPage>,
                    PostPage::class.java,
                    baseCategoryPath
            )
            collection.label = "Blog - ${it.title}"

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

