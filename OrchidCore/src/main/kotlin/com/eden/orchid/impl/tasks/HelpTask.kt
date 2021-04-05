package com.eden.orchid.impl.tasks

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OrchidFlags
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.jakewharton.picnic.table
import javax.inject.Inject
import javax.inject.Provider

@Description("Print the Orchid help page.")
class HelpTask
@Inject
constructor(
    private val tasks: Provider<Set<OrchidTask>>
) : OrchidTask("help", TaskService.TaskType.OTHER, 10) {

    override fun run(context: OrchidContext) {
        println(printHeader(context));
        println(
            table {
                header {
                    row("#", "alias", "description")
                }
                body {
                    row{
                        cell("Usage")
                        cell("-")
                        cell("  (orchid) <" + OrchidFlags.getInstance().positionalFlags.joinToString("> <") + "> [--<flag> <flag value>]")
                    }
                    row {
                        cell("Tasks") {
                            columnSpan = 2
                        }
                    }
                    tasks.get().forEach {
                        row {
                            cell(it.name)
                            cell("-")
                            cell(it.description)
                        }
                    }
                    row {
                        cell("Options") {
                            columnSpan = 2
                        }
                    }
                    OrchidFlags.getInstance().describeFlags().values.forEach {
                        var flagText = ""
                        if (!EdenUtils.isEmpty(it.aliases)) {
                            flagText = it.aliases.joinToString(", -")
                        }
                        row {
                            cell(it.key)
                            cell(flagText)
                            cell(it.description)
                        }
                    }

                }
            }
        )
    }

    private fun printHeader(context: OrchidContext): String {
        return "\n\nOrchid Static Site Generator.\nVersion " + context.site.orchidVersion + "\n"
    }

}
