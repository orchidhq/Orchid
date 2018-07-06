package com.eden.orchid.writersblocks.tags

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import java.time.format.DateTimeParseException
import javax.inject.Inject

class YoutubeTag @Inject
constructor() : TemplateTag("youtube", TemplateTag.Type.Simple, true) {

    @Option
    @Description("The Youtube video Id.")
    lateinit var id: String

    @Option
    @Description("The start time of the video, in MM:SS format.")
    lateinit var start: String

    @Option @StringDefault("560")
    @Description("The width of the embedded video.")
    lateinit var width: String

    @Option @StringDefault("315")
    @Description("The height of the embedded video.")
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
