package com.eden.orchid.sourcedoc

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option

class SourcedocFlags : OrchidFlag() {

    @Option("experimentalSourceDoc") @BooleanDefault(false)
    @Description("Feature flag to use new, experimental sourcedoc generators.")
    @JvmField
    var experimentalSourceDoc: Boolean = false

}
