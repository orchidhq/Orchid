package com.eden.orchid.impl.tasks

import com.copperleaf.krow.krow
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import com.google.inject.Provider
import javax.inject.Inject

@Description("Print the Orchid help page.")
class HelpTask
@Inject
constructor(
    private val tasks: Provider<Set<OrchidTask>>
) : OrchidTask(10, "help", TaskService.TaskType.OTHER) {

    override fun run(context: OrchidContext) {
        println(printHeader(context))
        println("Usage:")
        println(printUsage())
        println("Tasks:")
        println(printTasks())
        println("Options:")
        println(printOptions())
    }

    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    private fun printHeader(context: OrchidContext): String {
        return "\n\nOrchid Static Site Generator.\nVersion " + context.site.orchidVersion + "\n"
    }

    private fun printUsage(): String? {
        return krow {
            showHeaders = false
            showLeaders = false

            cell("key", "value") {
                content =
                    "  (orchid) <" + OrchidFlags.getInstance().positionalFlags.joinToString("> <") + "> [--<flag> <flag value>]"
                paddingLeft = 4
            }
            table {
                wrapTextAt = 80
            }
        }.print(OrchidUtils.compactTableFormatter)
    }

    private fun printTasks(): String? {
        return krow {
            showHeaders = false
            showLeaders = false

            for (task in tasks.get()) {
                cell("key", task.name) {
                    content = task.name
                    paddingLeft = 4
                }
                cell("description", task.name) {
                    content = task.description
                }
            }
            table {
                wrapTextAt = 80
            }

        }.print(OrchidUtils.compactTableFormatter)
    }

    private fun printOptions(): String? {
        return krow {
            showHeaders = false
            showLeaders = false

            for (flag in OrchidFlags.getInstance().describeFlags().values) {
                cell("key", flag.key) {
                    var flagText = "--" + flag.key
                    if (!EdenUtils.isEmpty(flag.aliases)) {
                        flagText += ", -" + flag.aliases.joinToString(", -")
                    }
                    content = flagText
                    paddingLeft = 4
                }
                cell("description", flag.key) {
                    content = flag.description
                }
            }
            table {
                wrapTextAt = 80
            }
        }.print(OrchidUtils.compactTableFormatter)
    }

}
