package com.eden.orchid.impl.tasks

import com.eden.orchid.Orchid
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.events.On
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.tasks.OrchidTask
import com.eden.orchid.api.tasks.TaskService
import com.eden.orchid.utilities.SuppressedWarnings
import com.google.inject.Provider
import java.util.EventListener
import java.util.Scanner
import javax.inject.Inject

@Description("Starts an interactive shell to run Orchid commands. Exit the interactive session with `quit`.")
class ShellTask
@Inject
constructor(
        private val contextProvider: Provider<OrchidContext>
) : OrchidTask(100, "shell", TaskService.TaskType.OTHER), EventListener {

    private var sn: Scanner? = null

    override fun run() {
        contextProvider.get().initOptions()

        sn = Scanner(System.`in`)
        while (true) {
            println("Enter a command:")
            print("> ")
            contextProvider.get().runCommand(sn!!.nextLine())
        }
    }

    @Suppress(SuppressedWarnings.UNUSED_PARAMETER)
    @On(Orchid.Lifecycle.EndSession::class)
    fun onEndSession(event: Orchid.Lifecycle.EndSession) {
        sn!!.close()
    }

}

