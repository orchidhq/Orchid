package com.eden.orchid.api.site

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.tasks.TaskService.TaskType
import com.google.inject.name.Named
import javax.inject.Inject

class DevServerBaseUrlFactory
@Inject
constructor(
    @Named("port") val port: Int
) : BaseUrlFactory("devServer", 100) {

    override fun isEnabled(context: OrchidContext): Boolean = context.taskType == TaskType.SERVE
    override fun getBaseUrl(context: OrchidContext): String = "http://localhost:${port}/"
}
