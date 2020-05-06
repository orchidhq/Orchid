package com.eden.orchid.posts

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.render.RenderService
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.posts.model.FeedsModel
import com.eden.orchid.utilities.SuppressedWarnings

@Description("Generate feeds for you blog in RSS and Atom formats.", name = "RSS Feeds")
class FeedsGenerator : OrchidGenerator<FeedsModel>(GENERATOR_KEY, Stage.META) {

    companion object {
        const val GENERATOR_KEY = "feeds"
    }

    @Option
    @StringDefault("rss", "atom")
    @Description(
        "A list of different feed types to render. Each feed type is rendered as `/{feedType}.xml` from the " +
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

    override fun startIndexing(context: OrchidContext): FeedsModel {
        return FeedsModel(context, createFeeds(context))
    }

    private fun createFeeds(context: OrchidContext): List<FeedPage> {
        val enabledGeneratorKeys = context.getGeneratorKeys(includeFrom, null)
        val feedItems = context.index.getChildIndices(enabledGeneratorKeys).flatMap { it.allPages }

        return if (feedItems.isNotEmpty()) {
            val sortedFeedItems = feedItems
                .sortedWith(compareBy({ it.lastModifiedDate }, { it.publishDate }))
                .reversed()
                .take(size)

            feedTypes
                .map { feedType -> feedType to context.getResourceEntry("feeds/$feedType.peb", null) }
                .filter { it.second != null }
                .map { (feedType, res) ->
                    res!!
                    res.reference.fileName = feedType
                    res.reference.path = ""
                    res.reference.outputExtension = "xml"
                    res.reference.isUsePrettyUrl = false

                    FeedPage(res, feedType, sortedFeedItems)
                }
        } else {
            emptyList()
        }
    }

    @Description(value = "A page with an RSS-like feed.", name = "RSS Feed")
    class FeedPage

    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    constructor(
        resource: OrchidResource,
        filename: String,
        val items: List<OrchidPage>
    ) : OrchidPage(resource, RenderService.RenderMode.RAW, "rss", null) {

        @Option
        lateinit var mimeType: String

        @Option
        lateinit var feedName: String
    }

}

