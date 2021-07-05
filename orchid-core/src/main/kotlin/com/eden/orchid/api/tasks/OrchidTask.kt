package com.eden.orchid.api.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY

abstract class OrchidTask
@JvmOverloads
constructor(
    open val name: String,
    open val taskType: TaskService.TaskType,
    priority: Int = DEFAULT_PRIORITY
) : Prioritized(priority), Descriptive {

    abstract fun run(context: OrchidContext)
}
