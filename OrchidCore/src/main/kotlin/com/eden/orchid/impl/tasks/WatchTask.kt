package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.google.inject.Provider
import javax.inject.Inject

@Description("Makes it easier to create content for your Orchid site by watching your resources for changes and " + "rebuilding the site on any changes.")
class WatchTask
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>
) : OrchidTask(100, "watch", TaskService.TaskType.WATCH) {

    override fun run() {
        contextProvider.get().build()
        contextProvider.get().watch()
    }

}

