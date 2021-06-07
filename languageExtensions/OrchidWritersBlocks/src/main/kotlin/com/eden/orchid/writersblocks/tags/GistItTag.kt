package com.eden.orchid.writersblocks.tags

import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

@Description("Embed a GitHub code snippet in your page.", name = "Gist-It")
class GistItTag : TemplateTag("gistit", Type.Simple, true) {

    @Option
    @Description("The owner of the repository")
    lateinit var owner: String

    @Option
    @Description("The repository name")
    lateinit var repository: String

    @Option
    @Description("The branch for the file")
    @StringDefault("master")
    lateinit var branch: String

    @Option
    @Description("The file path")
    lateinit var file: String

    @Option
    @Description(
        "Line slice. Format is a number for a single line or start:end. Negative indexing is supported (e.g. 0:-2)"
    )
    @StringDefault("")
    lateinit var slice: String

    override fun parameters() = arrayOf(
        ::owner.name,
        ::repository.name,
        ::branch.name,
        ::file.name,
        ::slice.name
    )
}
