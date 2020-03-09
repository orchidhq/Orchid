package com.eden.orchid.impl.commands

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand

@Description("Run the main Orchid build process.")
class BuildCommand : OrchidCommand("build") {

    override fun parameters() = emptyArray<String>()

    @Throws(Exception::class)
    override fun run(context: OrchidContext, commandName: String) {
        context.build()
    }
}

