package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option

@Description("Embed a Spotify track or playlist in your page.", name = "Spotify")
class SpotifyTag  : TemplateTag("spotify", Type.Simple, true) {

    @Option
    @Description("The Spotify object type: track | playlist")
    lateinit var type: String

    @Option
    @Description("The Spotify track or playlist ID.")
    lateinit var id: String

    override fun parameters() = arrayOf(::type.name, ::id.name)
}
