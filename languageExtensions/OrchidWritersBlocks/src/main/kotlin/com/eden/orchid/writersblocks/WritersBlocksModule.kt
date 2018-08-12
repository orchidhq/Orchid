package com.eden.orchid.writersblocks

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.writersblocks.functions.Nl2brFunction
import com.eden.orchid.writersblocks.functions.PluralizeFunction
import com.eden.orchid.writersblocks.tags.AccordionTag
import com.eden.orchid.writersblocks.tags.AlertTag
import com.eden.orchid.writersblocks.tags.GistTag
import com.eden.orchid.writersblocks.tags.InstagramTag
import com.eden.orchid.writersblocks.tags.TabsTag
import com.eden.orchid.writersblocks.tags.TwitterTag
import com.eden.orchid.writersblocks.tags.YoutubeTag

class WritersBlocksModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<TemplateFunction>(
                PluralizeFunction::class,
                Nl2brFunction::class)
        addToSet<TemplateTag>(
                AccordionTag::class,
                AlertTag::class,
                GistTag::class,
                InstagramTag::class,
                TabsTag::class,
                TwitterTag::class,
                YoutubeTag::class)
    }
}

