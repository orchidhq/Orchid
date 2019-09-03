package com.eden.orchid.sourcedoc.model

import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option

class SourceDocModuleConfig : OptionsHolder {

    @Option
    @Description("The source directories to document.")
    lateinit var name: String

    @Option
    @Description("The source directories to document.")
    lateinit var sourceDirs: List<String>

    @Option
    @BooleanDefault(true)
    @Description("Whether to reuse outputs from the cache, or rerun each build")
    var fromCache: Boolean = true

    @Option
    @BooleanDefault(false)
    var showRunnerLogs: Boolean = false

}
