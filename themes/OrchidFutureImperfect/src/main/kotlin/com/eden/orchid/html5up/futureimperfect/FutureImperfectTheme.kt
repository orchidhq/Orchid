package com.eden.orchid.html5up.futureimperfect

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.ComponentHolder
import com.eden.orchid.api.theme.menus.OrchidMenu
import com.eden.orchid.api.theme.models.SiteSocial
import javax.inject.Inject

@Description("A theme based on Future Imperfect by HTML5Up, good for blogs.", name = "FutureImperfect")
@Archetype(value = ConfigArchetype::class, key = "FutureImperfect")
class FutureImperfectTheme
@Inject
constructor(
    context: OrchidContext
) : Theme(context, "FutureImperfect") {

    @Option
    @Description("Your social media links.")
    var social: SiteSocial? = null
        get() {
            context.deprecationMessage { "Accessing `theme.social` is now deprecated. It should be configured and accessed at `site.about.social` instead" }
            return field?.takeIf { it.items.isNotEmpty() } ?: context.site.siteInfo.social
        }
        set(value) {
            field = value
        }

    @Option
    @Description("An additional menu to show in the side drawer.")
    var drawerMenu: OrchidMenu? = null

    @Option
    @Description("Components to include in the sidebar on pages with non-single layouts.")
    lateinit var sidebar: ComponentHolder

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss("assets/css/futureImperfect_main.scss")
        delegate.addCss("assets/css/futureImperfect_orchidCustomizations.scss")
        delegate.addCss("assets/css/orchidSearch.scss")

        delegate.addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/1.11.3/jquery.min.js")
        delegate.addJs("https://cdnjs.cloudflare.com/ajax/libs/skel/3.0.1/skel.min.js")
        delegate.addJs("assets/js/futureImperfect_util.js")
        delegate.addJs("assets/js/futureImperfect_main.js")
    }

    override fun getComponentHolders(): Array<ComponentHolder> {
        return super.getComponentHolders() + sidebar
    }

}
