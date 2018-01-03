package com.eden.orchid.changelog

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.changelog.model.ChangelogVersion
import com.eden.orchid.utilities.OrchidUtils
import java.util.stream.Stream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Description("Track changes and create references to all versions of your project.")
class ChangelogGenerator @Inject
constructor(context: OrchidContext, val model: ChangelogModel) : OrchidGenerator(context, "changelog", 700) {

    @Option("baseDir") @StringDefault("changelog")
    lateinit var changelogBaseDir: String

    override fun startIndexing(): List<OrchidPage> {
        val versions = context.getLocalResourceEntries(
                OrchidUtils.normalizePath(changelogBaseDir),
                context.compilerExtensions.toTypedArray(),
                false)
                .map { ChangelogVersion(context, it.reference.originalFileName, it) }
                .sortedBy { it.version }
                .reversed()

        model.initialize(versions)

        return emptyList()
    }

    override fun startGeneration(pages: Stream<out OrchidPage>) {

    }
}

