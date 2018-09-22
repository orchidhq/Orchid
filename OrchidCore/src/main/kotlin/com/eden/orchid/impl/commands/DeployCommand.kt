package com.eden.orchid.impl.commands

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.tasks.OrchidCommand
import com.google.inject.Provider
import javax.inject.Inject

@Description("Publish the Orchid build results.")
class DeployCommand
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>
) : OrchidCommand(100, "deploy") {

    @Option
    @BooleanDefault(true)
    @Description("Whether to run a dry deploy, validating all options but not actually deploying anything.")
    var isDry: Boolean = false

    override fun parameters(): Array<String> {
        return arrayOf("dry")
    }

    @Throws(Exception::class)
    override fun run(commandName: String) {
        contextProvider.get().deploy(isDry)
    }
}

