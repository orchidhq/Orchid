package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import javax.inject.Inject

@Description("Embed a Spotify track or playlist in your page.", name = "Spotify")
class SpotifyTag
@Inject
constructor(

) : TemplateTag("spotify", TemplateTag.Type.Simple, true) {

    @Option
    @Description("The Spotify object type: track | playlist")
    lateinit var type: String

    @Option
    @Description("The Spotify track or playlist ID.")
    lateinit var id: String

    override fun parameters(): Array<String> {
        return arrayOf("type", "id")
    }
}
