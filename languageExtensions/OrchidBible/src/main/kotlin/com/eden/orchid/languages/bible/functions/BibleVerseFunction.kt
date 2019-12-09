package com.eden.orchid.languages.bible.functions

import com.eden.Eden
import com.eden.americanbiblesociety.ABSRepository
import com.eden.bible.AbstractVerse
import com.eden.common.util.EdenUtils
import com.eden.interfaces.VerseFormatter
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.repositories.EdenRepository
import javax.inject.Inject
import javax.inject.Named

@Description("Easily embed full Bible verses in your content.", name = "Bible Verse")
class BibleVerseFunction
@Inject
constructor(
    @Named("absApiKey") val absApiKey: String,
    @Named("absDefaultVersion") val absDefaultVersion: String
) : TemplateFunction("bible", true), VerseFormatter {

    @Option
    @Description("The input to encode.")
    lateinit var verseReference: String

    @Option
    @Description("The Bible version key.")
    lateinit var version: String

    override fun parameters(): Array<String> {
        return arrayOf("verseReference", "version")
    }

    override fun apply(context: OrchidContext, page: OrchidPage?): Any? {
        if(!EdenUtils.isEmpty(absApiKey)) {
            val eden = Eden.getInstance()
            Eden.getInstance().config().putString("ABS_ApiKey", absApiKey)

            var repo: EdenRepository? = eden.getRepository(ABSRepository::class.java)
            if (repo == null) {
                try {
                    eden.registerRepository(ABSRepository())
                    repo = eden.getRepository(ABSRepository::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (repo != null) {
                repo.defaultBibleId = if(version.isNotBlank()) version else absDefaultVersion
                repo.setSelectedBible(repo.getBible(repo.defaultBibleId))
                val passage = repo.lookupVerse(verseReference)
                passage.verseFormatter = this
                return passage.text.split("<br/><i>")[0] + " ~ " + passage.reference.toString()
            }
        }

        return verseReference
    }

    override fun onFormatText(p0: String?): String {
        return p0 ?: ""
    }

    override fun onPreFormat(p0: AbstractVerse?): String {
        return ""
    }

    override fun onFormatVerseEnd(): String {
        return ""
    }

    override fun onPostFormat(): String {
        return ""
    }

    override fun onFormatVerseStart(p0: Int): String {
        return ""
    }

}
