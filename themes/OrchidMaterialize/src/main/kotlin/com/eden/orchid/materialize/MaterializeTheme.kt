package com.eden.orchid.materialize

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import javax.inject.Inject

class MaterializeTheme @Inject
constructor(context: OrchidContext) : Theme(context, "Materialize", 100) {

    @Option
    @Description("The site's colors, set up using Materialize color values.")
    var colors: MaterializeColors? = null

    @Option
    @Description("The site's shades, set up using Materialize shade values.")
    var shades: MaterializeShades? = null

    override fun loadAssets() {
        addCss("assets/css/appStyles.scss")
        addJs("assets/js/jquery-2.1.1.min.js")
        addJs("assets/js/materialize.min.js")
    }

    class MaterializeColors : OptionsHolder {

        @Option @StringDefault("cyan")
        @Description("The primary color.")
        lateinit var primary: String

        @Option @StringDefault("orange")
        @Description("The secondary color.")
        lateinit var secondary: String

        @Option @StringDefault("green")
        @Description("The success color.")
        lateinit var success: String

        @Option @StringDefault("red")
        @Description("The error color.")
        lateinit var error: String

        @Option @StringDefault("light-blue")
        @Description("The link color.")
        lateinit var link: String

    }

    class MaterializeShades : OptionsHolder {

        @Option @StringDefault("lighten-2")
        @Description("The primary shade.")
        lateinit var primary: String

        @Option @StringDefault("lighten-1")
        @Description("The secondary shade.")
        lateinit var secondary: String

        @Option @StringDefault("base")
        @Description("The success shade.")
        lateinit var success: String

        @Option @StringDefault("base")
        @Description("The error shade.")
        lateinit var error: String

        @Option @StringDefault("darken-1")
        @Description("The link shade.")
        lateinit var link: String

    }

}
