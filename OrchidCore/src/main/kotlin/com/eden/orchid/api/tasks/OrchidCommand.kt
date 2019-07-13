package com.eden.orchid.api.tasks

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.Descriptive
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.registration.Prioritized

/**
 * A Command represents something that can be executed via user-input in an interactive session (such as the
 * "interactive" task or from the admin command bar). Commands are very similar to tasks, but differ in the following
 * ways:
 * - Orchid runs a single Task in a session, but multiple Commands may be issued in a session
 * - A Task shuts Orchid down after it completes, but a command does not
 * - A Task does not have additional parameters passed to it; rather the command-line flags act as the Task options. In
 * contrast, additional parameters may be passed to a command directly, being extracted as an OptionsHolder
 * - A Task expects a regular String name, while Commands may be matched against a Regex pattern
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
abstract class OrchidCommand(priority: Int, val key: String) : Prioritized(priority), OptionsHolder, Descriptive {

    abstract fun parameters(): Array<String>

    abstract fun run(context: OrchidContext, commandName: String)

}
