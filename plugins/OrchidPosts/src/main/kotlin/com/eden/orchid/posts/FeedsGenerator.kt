package com.eden.orchid.posts

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.PostsModel
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Generate feeds for you blog in RSS and Atom formats.")
class FeedsGenerator
@Inject
constructor(
        context: OrchidContext,
        val postsModel: PostsModel)
    : OrchidGenerator(context, "feeds", OrchidGenerator.PRIORITY_LATE + 1) {

    @Option @StringDefault("rss", "atom")
    var feedTypes: Array<String> = emptyArray()

    @Option @StringDefault("posts")
    lateinit var includeFrom: Array<String>

    @Option @IntDefault(25)
    var size = 25

    override fun startIndexing(): List<OrchidPage>? {
        return null
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        generateFeeds()
    }

    override fun getCollections(): List<OrchidCollection<*>>? {
        return null
    }

    private fun generateFeeds() {
        val enabledGeneratorKeys = context.getGeneratorKeys(includeFrom, null)
        var feedItems = context.internalIndex.getGeneratorPages(enabledGeneratorKeys)

        var sortedFeedItems = feedItems
                .sortedWith(compareBy({ it.lastModifiedDate }, { it.publishDate }))
                .reversed()
                .take(size)

        feedTypes.forEach { feedType ->
            context.renderRaw(FeedPage(context, feedType, sortedFeedItems))
        }
    }

    class FeedPage constructor(context: OrchidContext, filename: String, val items: List<OrchidPage>) : OrchidPage(context.getResourceEntry("feeds/$filename.peb"), "rss") {
        init {
            this.reference.fileName = filename
            this.reference.path = ""
            this.reference.outputExtension = "xml"
            this.reference.isUsePrettyUrl = false
        }
    }

}

