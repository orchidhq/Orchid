package com.eden.orchid.presentations.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.presentations.model.Presentation
import javax.inject.Inject

@Description("Embed a Deck.js presentation.", name = "Presentation")
class PresentationComponent : OrchidComponent("presentation", 25) {

    @Option
    @StringDefault("web-2_0")
    @Description("The Deck.js presentation theme to use. Should be one of ['neon', 'swiss', 'web-2_0']")
    lateinit var deckTheme: String

    @Option
    @StringDefault("horizontal-slide")
    @Description("The Deck.js transition theme to use. Should be one of ['fade', 'horizontal-slide', 'vertical-slide']")
    lateinit var transitionTheme: String

    @Option
    @BooleanDefault(false)
    @Description("If true, only include the Deck.js Javascript files, opting to build the styles yourself.")
    var scriptsOnly: Boolean = false

    @Option
    @Description("The key of the Presentation to display.")
    var presentation: Presentation? = null

    override fun loadAssets() {
        if (!scriptsOnly) {
            addCss("assets/core/deck_core.scss")
            addCss("assets/extensions/goto/deck_goto.scss")
            addCss("assets/extensions/menu/deck_menu.scss")
            addCss("assets/extensions/navigation/deck_navigation.scss")
            addCss("assets/extensions/status/deck_status.scss")
            addCss("assets/extensions/scale/deck_scale.scss")
            addCss("assets/themes/style/$deckTheme.scss")
            addCss("assets/themes/transition/$transitionTheme.scss")
        }

        addJs("assets/vendor/modernizr_custom.js")
        addJs("assets/core/deck_core.js")
        addJs("assets/extensions/menu/deck_menu.js")
        addJs("assets/extensions/goto/deck_goto.js")
        addJs("assets/extensions/status/deck_status.js")
        addJs("assets/extensions/navigation/deck_navigation.js")
        addJs("assets/extensions/scale/deck_scale.js")
        addJs("assets/initDeck.js")
    }

}
