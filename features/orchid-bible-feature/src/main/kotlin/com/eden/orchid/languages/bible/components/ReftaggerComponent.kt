package com.eden.orchid.languages.bible.components

import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.OrchidComponent

@Description(
    "Add the Faithlife Reftagger script to your page, to automatically find and link Bible verses.",
    name = "Faithlife Reftagger"
)
class ReftaggerComponent : OrchidComponent("reftagger", true) {

    enum class BibleVersion(val value: String) {
        AMP("AMP"),
        ASV("ASV"),
        Darby("DAR"),
        ESV("ESV"),
        GW("GW"),
        HCSB("HCSB"),
        KJV("KJV"),
        LEB("LEB"),
        Message("MESSAGE"),
        NASB("NASB"),
        NCV("NCV"),
        NIV("NIV"),
        NIRV("NIRV"),
        NKJV("NKJV"),
        NLT("NLT"),
        DouayRheims("DOUAYRHEIMS"),
        YLT("YLT")
    }

    enum class BibleReader(val value: String) {
        Biblia("biblia"),
        Faithlife("bible.faithlife")
    }

    @Option
    @Description("The base URL to load Kotlin Playground JS files from.")
    @StringDefault("https://unpkg.com/kotlin-playground@1")
    lateinit var reftaggerSource: String

    @Option
    @StringDefault("ESV")
    lateinit var bibleVersion: BibleVersion

    @Option
    @StringDefault("Biblia")
    lateinit var bibleReader: BibleReader

    @Option
    @BooleanDefault(false)
    var roundedCorners: Boolean = false

    @Option
    @BooleanDefault(true)
    var dropShadow: Boolean = true

    @Option
    @BooleanDefault(false)
    var darkMode: Boolean = false

    @Option
    @BooleanDefault(false)
    var openInNewWindow: Boolean = false

    @Option
    lateinit var excludeTags: List<String>

    @Option
    lateinit var excludeClasses: List<String>

    override fun loadAssets(delegate: AssetManagerDelegate): Unit = with(delegate) {
        addJs("assets/js/reftagger.js") { inlined = true }
    }

    fun excludeTagsFormatted() = excludeTags.joinToString { "\"$it\"" }

    fun excludeClassesFormatted() = excludeTags.joinToString { "\"$it\"" }

    override fun isHidden(): Boolean {
        return true
    }
}
