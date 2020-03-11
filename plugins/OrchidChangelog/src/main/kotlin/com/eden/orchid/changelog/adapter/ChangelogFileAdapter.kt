package com.eden.orchid.changelog.adapter

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.StringResource
import com.eden.orchid.api.theme.pages.OrchidReference
import com.eden.orchid.changelog.ChangelogGenerator
import com.eden.orchid.changelog.model.ChangelogVersion
import com.eden.orchid.utilities.OrchidUtils

@Archetype(value = ConfigArchetype::class, key = ChangelogGenerator.GENERATOR_KEY)
class ChangelogFileAdapter : ChangelogAdapter {

    @Option
    @StringDefault("CHANGELOG")
    @Description("The name of the changelog file.")
    lateinit var filename: String

    @Option
    @Description("The base directory to start searching for the changelog file.")
    lateinit var baseDir: String

    @Option
    @StringDefault("{major}.{minor}.{patch}")
    @Description("The format your changelog versions follow.")
    lateinit var format: String

    @Option
    @StringDefault("^#{1,2}\\s*?(.*?)\\s*?(?:[-/](.*?))?\$")
    @Description("The format your changelog version follow.")
    lateinit var versionRegex: String

    @Option
    @IntDefault(1)
    var versionRegexGroup: Int = 1

    @Option
    @IntDefault(2)
    var releaseDateRegexGroup: Int = 2

    override fun getType() = "file"

    override fun loadChangelogEntries(context: OrchidContext): List<ChangelogVersion> {
        val readme = context.locateLocalResourceEntry(filename, context.compilerExtensions.toTypedArray())
            ?: context.findClosestFile(baseDir, filename, false, 10)

        if(readme == null) {
            Clog.w("Changelog file not found")
            return emptyList()
        }

        val regex = versionRegex.toRegex(RegexOption.MULTILINE)
        var currentVersion: String? = null
        var currentVersionReleaseDate: String? = null
        var previousIndex = 0

        val content = readme.content

        val changelogVersions = mutableListOf<ChangelogVersion>()

        regex.findAll(content).forEach {
            if(currentVersion != null) {
                changelogVersions += ChangelogVersion(
                    context,
                    format,
                    currentVersion!!.trim(),
                    currentVersionReleaseDate?.trim(),
                    StringResource(
                        OrchidReference(context, "${currentVersion}.${readme.reference.extension}"),
                        content.substring(previousIndex, it.range.first)
                    )
                )
            }

            currentVersion = it.groupValues[versionRegexGroup]
            currentVersionReleaseDate = it.groupValues[releaseDateRegexGroup]
            previousIndex = it.range.last + 1
        }

        changelogVersions += ChangelogVersion(
            context,
            format,
            currentVersion!!.trim(),
            currentVersionReleaseDate?.trim(),
            StringResource(
                OrchidReference(context, "${currentVersion}.${readme.reference.extension}"),
                content.substring(previousIndex)
            )
        )

        return changelogVersions
    }
}
