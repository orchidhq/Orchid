package com.eden.orchid.impl.commands

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.tasks.OrchidCommand

@Description("Publish the Orchid build results.")
class DeployCommand : OrchidCommand(100, "deploy") {

    @Option
    @BooleanDefault(true)
    @Description("Whether to run a dry deploy, validating all options but not actually deploying anything.")
    var dry: Boolean = true

    override fun parameters(): Array<String> {
        return arrayOf("dry")
    }

    @Throws(Exception::class)
    override fun run(context: OrchidContext, commandName: String) {
        context.deploy(dry)
    }
}

