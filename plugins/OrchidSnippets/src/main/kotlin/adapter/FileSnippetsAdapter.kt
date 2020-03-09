package com.eden.orchid.snippets.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource
import com.eden.orchid.snippets.models.SnippetConfig

@Description("Load each file as a separate snippet")
class FileSnippetsAdapter : SnippetsAdapter {

    override fun getType(): String = "file"

    @Option
    @Description("The base directory in resources to look for snippets files in.")
    @StringDefault("snippets")
    lateinit var baseDirs: List<String>

    @Option
    @Description("File extensions to filter resources by.")
    lateinit var fileExtensions: List<String>

    @Option
    @Description("Whether to search recursively")
    var recursive: Boolean = false

    override fun addSnippets(context: OrchidContext) : Sequence<SnippetConfig> = sequence {
        baseDirs.forEach { baseDir ->
            addSnippetsInDir(context, baseDir)
        }
    }

    private suspend fun SequenceScope<SnippetConfig>.addSnippetsInDir(context: OrchidContext, baseDir: String) {
        context
            .getResourceEntries(
                baseDir,
                fileExtensions.takeIf { it.isNotEmpty() }?.toTypedArray(),
                recursive,
                LocalResourceSource
            )
            .forEach { resource ->
                yield(
                    SnippetConfig(
                        context,
                        resource.reference.originalFileName,
                        emptyList(),
                        resource
                    )
                )
            }
    }
}
