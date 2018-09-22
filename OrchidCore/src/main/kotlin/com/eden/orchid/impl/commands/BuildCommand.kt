package com.eden.orchid.impl.commands

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidCommand
import com.google.inject.Provider

import javax.inject.Inject

@Description("Run the main Orchid build process.")
class BuildCommand
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>
) : OrchidCommand(100, "build") {

    override fun parameters(): Array<String> {
        return emptyArray()
    }

    @Throws(Exception::class)
    override fun run(commandName: String) {
        contextProvider.get().build()
    }
}

