package com.eden.orchid.changelog.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.changelog.ChangelogGenerator
import com.eden.orchid.changelog.model.ChangelogVersion
import com.eden.orchid.utilities.OrchidUtils

@Archetype(value = ConfigArchetype::class, key = ChangelogGenerator.GENERATOR_KEY)
class ChangelogDirAdapter : ChangelogAdapter {

    @Option
    @StringDefault("changelog")
    @Description("The base directory in local resources to look for changelog entries in.")
    lateinit var baseDir: String

    @Option
    @StringDefault("{major}.{minor}.{patch}")
    @Description("The format your changelog version follow.")
    lateinit var format: String

    override fun getType() = "directory"

    override fun loadChangelogEntries(context: OrchidContext): List<ChangelogVersion> {
        return context.getLocalResourceEntries(
            OrchidUtils.normalizePath(baseDir),
            context.compilerExtensions.toTypedArray(),
            true
        ).map { ChangelogVersion(context, format, it.reference.originalFileName, null, it) }
    }
}
