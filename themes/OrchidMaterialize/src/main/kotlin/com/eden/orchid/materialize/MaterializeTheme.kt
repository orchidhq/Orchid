package com.eden.orchid.materialize

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import javax.inject.Inject

class MaterializeTheme @Inject
constructor(context: OrchidContext) : Theme(context, "Materialize", 100) {

    @Option
    var colors: MaterializeColors? = null

    @Option
    var shades: MaterializeShades? = null

    override fun loadAssets() {
        addCss("assets/css/appStyles.scss")
        addJs("assets/js/jquery-2.1.1.min.js")
        addJs("assets/js/materialize.min.js")
    }

    class MaterializeColors : OptionsHolder {

        @Option
        @StringDefault("cyan")
        lateinit var primary: String

        @Option
        @StringDefault("orange")
        lateinit var secondary: String

        @Option
        @StringDefault("green")
        lateinit var success: String

        @Option
        @StringDefault("red")
        lateinit var error: String

        @Option
        @StringDefault("light-blue")
        lateinit var link: String

    }

    class MaterializeShades : OptionsHolder {

        @Option
        @StringDefault("lighten-2")
        lateinit var primary: String

        @Option
        @StringDefault("lighten-1")
        lateinit var secondary: String

        @Option
        @StringDefault("base")
        lateinit var success: String

        @Option
        @StringDefault("base")
        lateinit var error: String

        @Option
        @StringDefault("darken-1")
        lateinit var link: String

    }

}
