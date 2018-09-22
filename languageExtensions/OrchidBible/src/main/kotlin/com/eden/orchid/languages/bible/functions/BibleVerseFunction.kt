package com.eden.orchid.languages.bible.functions

import com.eden.Eden
import com.eden.americanbiblesociety.ABSRepository
import com.eden.bible.AbstractVerse
import com.eden.common.util.EdenUtils
import com.eden.interfaces.VerseFormatter
import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.repositories.EdenRepository
import javax.inject.Inject

@Description("Easily embed full Bible verses in your content.", name = "Bible Verse")
class BibleVerseFunction
@Inject
constructor(

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

    override fun apply(): Any {
        try {
            if(!EdenUtils.isEmpty(Eden.getInstance().config().getString("ABS_ApiKey"))) {
                val eden = Eden.getInstance()
                eden.config().putString("com.eden.americanbiblesociety.ABSRepository_selectedBibleId", version)
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
                    val passage = repo.lookupVerse(verseReference)
                    passage.verseFormatter = this
                    return passage.text.split("<br/><i>")[0] + " ~ " + passage.reference.toString()
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
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
