package com.eden.orchid.impl.commands

import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand

@Description("Run the main Orchid build process.")
class QuitCommand : OrchidCommand(1000, "quit") {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    override fun run(context: OrchidContext, commandName: String) {
        context.broadcast(Orchid.Lifecycle.EndSession.fire(this))
    }
}

