package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService

@Description("Makes it easier to create content for your Orchid site by watching your resources for changes and " + "rebuilding the site on any changes.")
class WatchTask : OrchidTask(100, "watch", TaskService.TaskType.WATCH) {

    override fun run(context: OrchidContext) {
        context.build()
        context.watch()
    }

}

