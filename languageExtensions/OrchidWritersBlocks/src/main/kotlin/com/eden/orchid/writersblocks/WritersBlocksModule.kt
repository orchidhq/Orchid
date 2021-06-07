package com.eden.orchid.writersblocks

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.writersblocks.functions.EncodeSpacesFunction
import com.eden.orchid.writersblocks.functions.Nl2brFunction
import com.eden.orchid.writersblocks.functions.PluralizeFunction
import com.eden.orchid.writersblocks.tags.*

class WritersBlocksModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<TemplateFunction>(
            PluralizeFunction::class,
            Nl2brFunction::class,
            EncodeSpacesFunction::class
        )
        addToSet<TemplateTag>(
            AlertTag::class,
            GistItTag::class,
            GistTag::class,
            InstagramTag::class,
            SpotifyTag::class,
            TwitterTag::class,
            YoutubeTag::class
        )
    }
}

