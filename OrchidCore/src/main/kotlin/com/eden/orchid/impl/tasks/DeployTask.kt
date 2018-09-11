package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.google.inject.Provider

import javax.inject.Inject
import javax.inject.Named

@Description("Run the main Orchid build process, publish the results, then exit.")
class DeployTask
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>,
        @param:Named("dryDeploy") private val dryDeploy: Boolean
) : OrchidTask(100, "deploy", TaskService.TaskType.DEPLOY) {

    override fun run() {
        contextProvider.get().build()
        contextProvider.get().deploy(dryDeploy)
    }

}
