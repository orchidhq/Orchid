package com.eden.orchid.writersblocks.tags

import clog.Clog
import com.eden.orchid.api.compilers.TemplateTag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import java.time.format.DateTimeParseException
import java.util.Locale

@Description("Embed a YouTube video in your page.", name = "YouTube")
class YoutubeTag : TemplateTag("youtube", Type.Simple, true) {

    @Option
    @Description("The Youtube video Id.")
    lateinit var id: String

    @Option
    @Description("The start time of the video, in MM:SS format.")
    lateinit var start: String

    @Option
    @StringDefault("560")
    @Description("The width of the embedded video.")
    lateinit var width: String

    @Option
    @StringDefault("315")
    @Description("The height of the embedded video.")
    lateinit var height: String

    @Option
    @Description("The aspect ratio of the video in W:L, defaults to 16:9.")
    @StringDefault("")
    lateinit var aspectRatio: String

    @Option
    @Description("Allowed video attributes.")
    @StringDefault("accelerometer", "autoplay", "encrypted-media", "gyroscope", "picture-in-picture")
    lateinit var allow: List<String>

    override fun parameters() = arrayOf(::id.name, ::start.name, ::aspectRatio.name)

    fun getStartSeconds(): Int {
        if (start.isNotBlank() && start.contains(":")) {
            try {
                val time = start.split(":")
                if (time.size == 2) {
                    return (Integer.parseInt(time[0]) * (60)) + (Integer.parseInt(time[1]))
                } else if (time.size == 3) {
                    return (Integer.parseInt(time[0]) * (60 * 60)) + (Integer.parseInt(time[1]) * (60)) + (
                        Integer.parseInt(
                            time[2]
                        )
                        )
                }
            } catch (e: DateTimeParseException) {
                Clog.e(e)
            }
        }

        return 0
    }

    fun hasAspectRatio(): Boolean {
        return aspectRatio.isNotBlank()
    }

    fun getAspectRatioPercent(): String {
        if (aspectRatio.isNotBlank() && aspectRatio.contains(":")) {
            val (width, height) = aspectRatio.split(":").take(2).map { it.trim() }

            return "%.2f".format(Locale.ENGLISH, height.toDouble() / width.toDouble() * 100)
        }

        return "100"
    }
}
