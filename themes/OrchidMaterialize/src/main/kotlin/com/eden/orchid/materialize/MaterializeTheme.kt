package com.eden.orchid.materialize

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.OptionsHolder
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.Theme
import com.eden.orchid.api.theme.models.About

import javax.inject.Inject

class MaterializeTheme @Inject
constructor(context: OrchidContext) : Theme(context, "Materialize", 100) {

    @Option
    var about: About? = null

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
        var primary: String? = null

        @Option
        @StringDefault("orange")
        var secondary: String? = null

        @Option
        @StringDefault("green")
        var success: String? = null

        @Option
        @StringDefault("red")
        var error: String? = null

        @Option
        @StringDefault("light-blue")
        var link: String? = null

    }

    class MaterializeShades : OptionsHolder {

        @Option
        @StringDefault("lighten-2")
        var primary: String? = null

        @Option
        @StringDefault("lighten-1")
        var secondary: String? = null

        @Option
        @StringDefault("base")
        var success: String? = null

        @Option
        @StringDefault("base")
        var error: String? = null

        @Option
        @StringDefault("darken-1")
        var link: String? = null

    }

}
