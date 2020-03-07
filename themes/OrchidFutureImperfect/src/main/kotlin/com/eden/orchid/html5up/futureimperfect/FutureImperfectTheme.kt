package com.eden.orchid.html5up.futureimperfect

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.models.Social
import javax.inject.Inject

@Description("A theme based on Future Imperfect by HTML5Up, good for blogs.", name = "FutureImperfect")
@Archetype(value = ConfigArchetype::class, key = "FutureImperfect")
class FutureImperfectTheme
@Inject
constructor(
        context: OrchidContext
) : Theme(context, "FutureImperfect") {

    companion object {
        const val DEPRECATION_MESSAGE = "FutureImperfectTheme Theme configuration of search has been removes, and you must now " +
                "migrate to the `orchidSearch` or `algoliaDocsearch` meta-component config instead."
    }

    @Option
    @Description("Your social media links.")
    var social: Social? = null

    @Option
    @Description("An additional menu to show in the side drawer.")
    var drawerMenu: OrchidMenu? = null

    @Option
    @Description("Components to include in the sidebar on pages with non-single layouts.")
    lateinit var sidebar: ComponentHolder

    @Option
    @Description("Whether to use the legacy config for site search. NOTE: $DEPRECATION_MESSAGE")
    @BooleanDefault(true)
    @Deprecated(DEPRECATION_MESSAGE)
    var legacySearch: Boolean = true

    override fun loadAssets() {
        addCss("assets/css/futureImperfect_main.scss")
        addCss("assets/css/futureImperfect_orchidCustomizations.scss")
        addCss("assets/css/orchidSearch.scss")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/skel/3.0.1/skel.min.js")
        addJs("assets/js/futureImperfect_util.js")
        addJs("assets/js/futureImperfect_main.js")

        if(legacySearch) {
            Clog.e(DEPRECATION_MESSAGE)
        }
    }

    override fun getComponentHolders(): Array<ComponentHolder> {
        return super.getComponentHolders() + arrayOf(sidebar)
    }

}
