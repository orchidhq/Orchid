package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService

@Description("Run the main Orchid build process, then exit. This is the default task.")
class BuildTask : OrchidTask(100, "build", TaskService.TaskType.BUILD) {

    override fun run(context: OrchidContext) {
        context.build()
    }

}
