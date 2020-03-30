package com.eden.orchid.api.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY

abstract class OrchidCommand
@JvmOverloads
constructor(
    val key: String,
    priority: Int = DEFAULT_PRIORITY
) : Prioritized(priority), OptionsHolder, Descriptive {

    abstract fun parameters(): Array<String>

    abstract fun run(context: OrchidContext, commandName: String)

}
