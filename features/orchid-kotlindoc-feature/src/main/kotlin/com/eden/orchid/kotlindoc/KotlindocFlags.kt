package com.eden.orchid.kotlindoc

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected

class KotlindocFlags : OrchidFlag() {

    @Option
    @Description(
        "The classpath used for resolving references, passed directly to Dokka as the `-classpath` command " +
            "line argument. Typically used for passing the classpath directly from the build tool running Orchid. For " +
            "manual entry, set the args as `kotlindoc.args` in your `config.yml`."
    )
    @Protected
    lateinit var kotlindocClasspath: String
}
