package com.eden.orchid.bsdoc

import com.eden.common.util.EdenUtils
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.assets.CssPage
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.models.Social
import org.json.JSONObject
import javax.inject.Inject

@Description("A theme based on the Bootstrap 3 documentation, and good for code documentation.", name="BsDoc")
class BSDocTheme
@Inject
constructor(
        context: OrchidContext
) : Theme(context, "BsDoc", 100) {

    @Option
    @StringDefault("#4C376C")
    @Description("The CSS HEX value for the site's primary color.")
    lateinit var primaryColor: String

    @Option
    @StringDefault("#000000")
    @Description("The CSS HEX value for the site's secondary color.")
    lateinit var secondaryColor: String

    @Option
    @StringDefault("")
    @Description("The CSS HEX value for the site's link color.")
    lateinit var linkColor: String

    @Option
    @StringDefault("")
    @Description("The CSS HEX value for the site's GitHub banner background color.")
    lateinit var bannerColor: String

    @Option
    @Description("Your social media links.")
    var social: Social? = null

    @Option
    @Description("Github project for 'Fork me on Github' ribbon")
    lateinit var github: String

    @Option
    @Description("Custom options for Trianglify.")
    lateinit var trianglifyOptions: JSONObject

    @Option
    @BooleanDefault(true)
    @Description("Whether to show the current verision in the header and footer..")
    var showVersion: Boolean = true

    @Option
    @Description("Components to include in the sidebar, below the page menu.")
    lateinit var sidebar: ComponentHolder

    @Option
    @BooleanDefault(false)
    @Description("If true, extra CSS and Javascript will be included to support static searching with Lunr.js, and a " +
            "searchbar added above the sidenav menu."
    )
    var useSidebarSearch: Boolean = false

    override fun loadAssets() {
        // these assets include relative references to font files, which become invalid if the asset it downloaded locally and so need to stay as external assets even in production
        addCss(CssPage(this, "theme", context.getResourceEntry("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css"), "bootstrap.min", null))
        addCss(CssPage(this, "theme", context.getResourceEntry("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"), "font-awesome.min", null))
        addCss("https://cdnjs.cloudflare.com/ajax/libs/github-fork-ribbon-css/0.2.0/gh-fork-ribbon.min.css")
        addCss("assets/css/bsdoc.scss")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js")
        addJs("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.3.0/bootbox.min.js")
        addJs("assets/js/bsdoc.js")

        if (useSidebarSearch) {
            addCss("assets/css/orchidSearch.scss")
            addJs("https://unpkg.com/lunr/lunr.js")
            addJs("assets/js/orchidSearch.js")
        }
    }

    override fun onPostExtraction() {
        super.onPostExtraction()

        if (EdenUtils.isEmpty(linkColor)) {
            linkColor = primaryColor
        }

        if (EdenUtils.isEmpty(bannerColor)) {
            bannerColor = secondaryColor
        }
    }

    override fun getComponentHolders(): Array<ComponentHolder> {
        return arrayOf(sidebar)
    }
}
