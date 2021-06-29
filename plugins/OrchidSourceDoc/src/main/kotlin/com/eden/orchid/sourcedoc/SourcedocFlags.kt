package com.eden.orchid.sourcedoc

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option

class SourcedocFlags : OrchidFlag() {

    @Option("experimentalSourceDoc") @BooleanDefault(false)
    @Description("Feature flag to use new, experimental sourcedoc generators.")
    @JvmField
    @Deprecated(
        "This is now the default, and the flag is ignored. If you have not migrated from the older " +
            "sourcedocs plugins, you can continue using them with `--legacySourceDoc`, but they will be removed " +
            "entirely in the next major release, 0.22.0.",
        replaceWith = ReplaceWith("!legacySourceDoc")
    )
    var experimentalSourceDoc: Boolean = false

    @Option("legacySourceDoc") @BooleanDefault(false)
    @Description("Feature flag to use old, legacy sourcedoc generators.")
    @JvmField
    var legacySourceDoc: Boolean = false
}
