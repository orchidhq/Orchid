package com.eden.orchid.impl.commands

import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand
import com.google.inject.Provider

import javax.inject.Inject

@Description("Run the main Orchid build process.")
class QuitCommand @Inject
constructor(private val contextProvider: Provider<OrchidContext>) : OrchidCommand(1000, "quit") {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    override fun run(commandName: String) {
        contextProvider.get().broadcast(Orchid.Lifecycle.EndSession.fire(this))
    }
}

