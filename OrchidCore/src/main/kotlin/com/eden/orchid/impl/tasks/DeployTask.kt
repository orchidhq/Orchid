package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import javax.inject.Inject
import javax.inject.Named

@Description("Run the main Orchid build process, publish the results, then exit.")
class DeployTask
@Inject
constructor(
    @Named("dryDeploy") private val dryDeploy: Boolean
) : OrchidTask(100, "deploy", TaskService.TaskType.DEPLOY) {

    override fun run(context: OrchidContext) {
        context.build()
        val success = context.deploy(dryDeploy)

        if (!success) {
            throw Exception("deployment failed, check logs for details")
        }
    }

}
