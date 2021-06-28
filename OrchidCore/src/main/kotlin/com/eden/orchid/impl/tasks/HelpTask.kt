package com.eden.orchid.impl.tasks

import com.copperleaf.krow.builder.bodyRow
import com.copperleaf.krow.builder.column
import com.copperleaf.krow.builder.krow
import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.utilities.OrchidUtils
import com.eden.orchid.utilities.SuppressedWarnings
import javax.inject.Inject
import javax.inject.Provider

@Description("Print the Orchid help page.")
class HelpTask
@Inject
constructor(
    private val tasks: Provider<Set<OrchidTask>>
) : OrchidTask("help", TaskService.TaskType.OTHER, 10) {

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
        return OrchidUtils.defaultTableFormatter.print(krow {
            includeHeaderRow = false
            includeLeadingColumn = false

            header {
                column("value") {
                    this.width = 80
                }
            }

            bodyRow("key") {
                cellAt("value") {
                    content = "  (orchid) <" + OrchidFlags.getInstance().positionalFlags.joinToString("> <") + "> [--<flag> <flag value>]"
                }
            }
        })
    }

    private fun printTasks(): String? {
        return OrchidUtils.defaultTableFormatter.print(krow {
            includeHeaderRow = false
            includeLeadingColumn = false

            header {
                column("key") {}
                column("description") {}
            }

            for (task in tasks.get()) {
                bodyRow(task.name) {
                    cellAt("key") {
                        content = task.name
                    }
                    cellAt("description") {
                        content = task.description
                    }
                }
            }
        })
    }

    private fun printOptions(): String? {
        return OrchidUtils.defaultTableFormatter.print(krow {
            includeHeaderRow = false
            includeLeadingColumn = false

            header {
                column("key") {}
                column("description") {}
            }

            for (flag in OrchidFlags.getInstance().describeFlags().values) {
                bodyRow(flag.key) {
                    cellAt("key") {
                        var flagText = "--" + flag.key
                        if (!EdenUtils.isEmpty(flag.aliases)) {
                            flagText += ", -" + flag.aliases.joinToString(", -")
                        }
                        content = flagText
                    }
                    cellAt("description") {
                        content = flag.description
                    }
                }
            }
        })
    }

}
