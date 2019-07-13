package com.eden.orchid.impl.commands

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand

@Description("Run the main Orchid build process.")
class BuildCommand : OrchidCommand(100, "build") {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    @Throws(Exception::class)
    override fun run(context: OrchidContext, commandName: String) {
        context.build()
    }
}

