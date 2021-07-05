package com.eden.orchid.impl.commands

import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand

@Description("Run the main Orchid build process.")
class QuitCommand : OrchidCommand("quit", 1000) {

    override fun parameters() = emptyArray<String>()

    override fun run(context: OrchidContext, commandName: String) {
        context.broadcast(Orchid.Lifecycle.EndSession.fire(this))
    }
}
