package com.eden.orchid.writersBlocks.tags

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import java.time.format.DateTimeParseException
import javax.inject.Inject

class YoutubeTag @Inject
constructor() : TemplateTag("youtube", false) {

    @Option
    lateinit var id: String

    @Option
    lateinit var start: String

    @Option @StringDefault("560")
    lateinit var width: String

    @Option @StringDefault("315")
    lateinit var height: String

    override fun parameters(): Array<String> {
        return arrayOf("id", "start")
    }

    public fun getStartSeconds(): Int {
        if(start.isNotBlank() && start.contains(":")) {
            try {
                val time = start.split(":")
                if(time.size == 2) {
                    return (Integer.parseInt(time[0]) * (60)) + (Integer.parseInt(time[1]))
                }
                else if(time.size == 3) {
                    return (Integer.parseInt(time[0]) * (60*60)) + (Integer.parseInt(time[1]) * (60)) + (Integer.parseInt(time[2]))
                }
            } catch (e: DateTimeParseException) {
                Clog.e(e.message, e)
            }
        }

        return 0
    }
}
