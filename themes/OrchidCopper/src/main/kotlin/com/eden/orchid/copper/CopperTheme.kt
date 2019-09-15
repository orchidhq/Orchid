package com.eden.orchid.copper

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.models.Social
import com.eden.orchid.impl.relations.AssetRelation
import com.eden.orchid.impl.relations.PageRelation
import javax.inject.Inject

@Description("A Bulma-based, any-purpose theme.", name="Copper")
class CopperTheme
@Inject
constructor(context: OrchidContext) : Theme(context, "Copper", 100) {

    @Option
    @Description("Your social media links.")
    lateinit var social: Social

    @Option
    @Description("The primary color values for your site, specified as any valid CSS color value.")
    lateinit var colors: CopperThemeColors

    @Option
    @Description("The color palette for your site, specified as the uppercased name of a color in the theme colors.")
    lateinit var palette: CopperThemePalette

    @Option @StringDefault("assets/media/theme-logo.png")
    @Description("An asset to use the for sidebar logo.")
    lateinit var logo: String

    @Option @StringDefault("assets/media/bg-texture.png")
    @Description("An asset to use the for background texture.")
    lateinit var backgroundTexture: String

    @Option @BooleanDefault(true)
    @Description("The whether to add a shadow to the sidebar.")
    var useBackgroundTexture: Boolean = true

    @Option @StringDefault("IMAGE")
    @Description("The type of background to use for the sidebar, one of [IMAGE, GRADIENT, SOLID]")
    lateinit var sidebarBackgroundType: CopperThemeSidebarBackgroundType

    @Option @StringDefault("assets/media/bg-sidebar.jpeg")
    @Description("The value to use for the sidebar background.")
    lateinit var sidebarBackground: String

    @Option @BooleanDefault(true)
    @Description("The whether to add a shadow to the sidebar.")
    var sidebarShadow: Boolean = true

    @Option
    lateinit var navbarPrimaryButtonTitle: String
    @Option
    lateinit var navbarPrimaryButton: PageRelation

    @Option
    lateinit var navbarSecondaryButtonTitle: String
    @Option
    lateinit var navbarSecondaryButton: PageRelation

    @Option @StringDefault("")
    lateinit var navbarLogo: AssetRelation

    override fun loadAssets() {
        addCss("assets/css/bulma.scss")
        addCss("assets/css/extraCss.scss")
        addCss("assets/css/bulma-tooltip.css")
        addCss("assets/css/bulma-accordion.min.css")

        addJs("https://use.fontawesome.com/releases/v5.4.0/js/all.js").apply { isDefer = true }
        addJs("assets/js/bulma.js")
        addJs("assets/js/bulma-accordion.min.js")
        addJs("assets/js/bulma-tabs.js")
    }
}

class CopperThemeColors : OptionsHolder {

    @Option
    @StringDefault("#E87B2E")
    lateinit var orange: String

    @Option
    @StringDefault("#DBD5AB")
    lateinit var yellow: String

    @Option
    @StringDefault("#8B8F5E")
    lateinit var green: String

    @Option
    @StringDefault("#56C2D9")
    lateinit var turquoise: String

    @Option
    @StringDefault("#6F8787")
    lateinit var cyan: String

    @Option
    @StringDefault("#7695B2")
    lateinit var blue: String

    @Option
    @StringDefault("#625D73")
    lateinit var purple: String

    @Option
    @StringDefault("#D97E74")
    lateinit var red: String

    @Option
    @StringDefault("#000000")
    lateinit var black: String

    @Option
    @StringDefault("#ffffff")
    lateinit var white: String

}

class CopperThemePalette : OptionsHolder {

    @Option
    @StringDefault("PURPLE")
    lateinit var primary: CopperThemeColorName

    @Option
    @StringDefault("BLUE")
    lateinit var info: CopperThemeColorName

    @Option
    @StringDefault("GREEN")
    lateinit var success: CopperThemeColorName

    @Option
    @StringDefault("YELLOW")
    lateinit var warning: CopperThemeColorName

    @Option
    @StringDefault("ORANGE")
    lateinit var danger: CopperThemeColorName

    @Option
    @StringDefault("BLUE")
    lateinit var code: CopperThemeColorName

    @Option
    @StringDefault("BLUE")
    lateinit var link: CopperThemeColorName

    @Option
    @StringDefault("PURPLE")
    lateinit var linkVisited: CopperThemeColorName

    @Option
    @StringDefault("BLACK")
    lateinit var black: CopperThemeColorName

    @Option
    @StringDefault("WHITE")
    lateinit var white: CopperThemeColorName

}

enum class CopperThemeColorName(val variableName: String) {

    ORANGE("\$orange"),
    YELLOW("\$yellow"),
    GREEN("\$green"),
    TURQUOISE("\$turquoise"),
    CYAN("\$cyan"),
    BLUE("\$blue"),
    PURPLE("\$purple"),
    RED("\$red"),
    BLACK("\$black"),
    WHITE("\$white")
    ;

    override fun toString() = variableName
}

enum class CopperThemeSidebarBackgroundType {

    IMAGE, GRADIENT, SOLID

}
