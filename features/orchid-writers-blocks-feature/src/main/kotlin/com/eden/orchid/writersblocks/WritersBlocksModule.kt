package com.eden.orchid.writersblocks

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.writersblocks.components.GoogleAnalyticsComponent
import com.eden.orchid.writersblocks.components.GoogleTagManagerComponent
import com.eden.orchid.writersblocks.functions.EncodeSpacesFunction
import com.eden.orchid.writersblocks.functions.Nl2brFunction
import com.eden.orchid.writersblocks.functions.PluralizeFunction
import com.eden.orchid.writersblocks.tags.AlertTag
import com.eden.orchid.writersblocks.tags.GistItTag
import com.eden.orchid.writersblocks.tags.GistTag
import com.eden.orchid.writersblocks.tags.SpotifyTag
import com.eden.orchid.writersblocks.tags.TwitterTag
import com.eden.orchid.writersblocks.tags.YoutubeTag

class WritersBlocksModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<TemplateFunction>(
            PluralizeFunction::class,
            Nl2brFunction::class,
            EncodeSpacesFunction::class
        )
        addToSet<OrchidComponent>(
            GoogleAnalyticsComponent::class,
            GoogleTagManagerComponent::class,
        )
        addToSet<TemplateTag>(
            AlertTag::class,
            GistItTag::class,
            GistTag::class,
            SpotifyTag::class,
            TwitterTag::class,
            YoutubeTag::class
        )
    }
}
