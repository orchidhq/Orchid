package com.eden.orchid.posts

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import java.util.stream.Stream
import javax.inject.Inject

@Description("Generate feeds for you blog in RSS and Atom formats.", name = "RSS Feeds")
class FeedsGenerator
@Inject
constructor(
        context: OrchidContext
) : OrchidGenerator(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_LATE + 1) {

    companion object {
        const val GENERATOR_KEY = "feeds"
    }

    @Option
    @StringDefault("rss", "atom")
    @Description("A list of different feed types to render. Each feed type is rendered as `/{feedType}.xml` from the " +
            "`feeds/{feedType}.peb` resource."
    )
    var feedTypes: Array<String> = emptyArray()

    @Option
    @StringDefault("posts")
    @Description("A list of generator keys whose pages are included in this feed.")
    lateinit var includeFrom: Array<String>

    @Option
    @IntDefault(25)
    @Description("The maximum number of entries to include in this feed.")
    var size = 25

    override fun startIndexing(): List<OrchidPage> {
        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {
        generateFeeds()
    }

    override fun getCollections(pages: List<OrchidPage>): List<OrchidCollection<*>> {
        return emptyList()
    }

    private fun generateFeeds() {
        val enabledGeneratorKeys = context.getGeneratorKeys(includeFrom, null)
        var feedItems = context.index.getChildIndices(enabledGeneratorKeys)

        if (feedItems.isNotEmpty()) {
            var sortedFeedItems = feedItems
                    .sortedWith(compareBy({ it.lastModifiedDate }, { it.publishDate }))
                    .reversed()
                    .take(size)

            feedTypes.forEach { feedType ->
                val res = context.getResourceEntry("feeds/$feedType.peb")
                if (res != null) {
                    context.renderRaw(FeedPage(res, feedType, sortedFeedItems))
                }
            }
        }
    }

    @Description(value = "A page with an RSS-like feed.", name = "RSS Feed")
    class FeedPage
    constructor(
            resource: OrchidResource,
            filename: String,
            val items: List<OrchidPage>
    ) : OrchidPage(resource, "rss", null) {
        init {
            this.reference.fileName = filename
            this.reference.path = ""
            this.reference.outputExtension = "xml"
            this.reference.isUsePrettyUrl = false
        }
    }

}

