package com.eden.orchid.impl.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService

@Description("Makes it easier to create content for your Orchid site by watching your resources for changes and " +
        "rebuilding the site on any changes. A static HTTP server is also created in the root of your site and " +
        "the baseUrl set to this server's address so you can preview the output. You can also access the admin " +
        "dashboard to get insight into your current Orchid setup and manage your content.")
class ServeTask : OrchidTask(100, "serve", TaskService.TaskType.SERVE) {

    override fun run(context: OrchidContext) {
        context.serve()
        context.build()
        context.watch()
    }

}

