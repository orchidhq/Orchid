package com.eden.orchid.impl.commands

import com.caseyjbrooks.clog.Clog
import com.copperleaf.krow.formatters.html.HtmlTableFormatter
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsExtractor
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.server.OrchidServer
import com.eden.orchid.api.tasks.OrchidCommand
import com.eden.orchid.utilities.OrchidUtils
import javax.inject.Inject

@Description("This will show a table will all the available options for a type, along with other relevant data.")
class HelpCommand
@Inject
constructor(
    private val server: OrchidServer?
) : OrchidCommand(100, "help") {

    enum class HelpType {
        CLASS, CONFIG, PAGE
    }

    @Option
    @Description(
        "CLASS: The fully-qualified name of a class to describe." +
                "CONFIG: An object query for a given property in the site config." +
                "PAGE: A page type."
    )
    lateinit var type: HelpType

    @Option
    lateinit var query: String

    override fun parameters(): Array<String> {
        return arrayOf("type", "query")
    }

    override fun run(context: OrchidContext, commandName: String) {
        val parsedClass = getDescribedClass()
        if (OptionsHolder::class.java.isAssignableFrom(parsedClass)) {
            val extractor = context.resolve(OptionsExtractor::class.java)
            val description = extractor.describeAllOptions(parsedClass)
            val table = extractor.getDescriptionTable(description)

            val asciiTable = table.print(OrchidUtils.compactTableFormatter)
            Clog.i("\n{}", asciiTable)

            if (server != null && server.websocket != null) {
                var htmlTable = table.print(HtmlTableFormatter())
                htmlTable = htmlTable.replace("<table>".toRegex(), "<table class=\"table\">")
                server.websocket.sendMessage("describe", htmlTable)
            }
        }
    }

    private fun getDescribedClass(): Class<*> {
        return Class.forName(query)
    }

}

