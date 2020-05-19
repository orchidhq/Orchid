package com.eden.orchid.sourcedoc.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Archetypes
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.sourcedoc.options.SourcedocPageConfigArchetype

@Archetypes(
    Archetype(value = SourcedocPageConfigArchetype::class, key = "defaultConfig")
)
abstract class SourceDocModuleConfig(val moduleType: String) : OptionsHolder {

    @Option
    @Description("The unique name of this module.")
    lateinit var name: String

    @Option
    @Description("The slug of this module.")
    lateinit var slug: String

    @Option
    @Description("An optional name to group sets of modules by.")
    lateinit var moduleGroup: String

    @Option
    @StringDefault(":moduleType/:moduleGroup/:module")
    @Description("Configure the permalink applied to this module's homepage.")
    lateinit var homePagePermalink: String

    @Option
    @StringDefault(":moduleType/:moduleGroup/:module/:sourceDocPath")
    @Description("Configure the permalink applied to this module's source docs pages.")
    lateinit var sourcePagePermalink: String

    @Option
    @Description("The source directories to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @Description("Other modules which contain sources that may be linked to by the page sin this module")
    lateinit var relatedModules: List<String>

    @Option
    @BooleanDefault(true)
    @Description("Whether to reuse outputs from the cache, or rerun each build")
    var fromCache: Boolean = true

    @Option
    @BooleanDefault(false)
    var showRunnerLogs: Boolean = false

    @Option
    @BooleanDefault(false)
    var homePageOnly: Boolean = false

    @Option
    @Description("Arbitrary command line arguments to pass through directly to Dokka.")
    lateinit var args: List<String>

    open fun additionalRunnerArgs() : List<String> = args

}
