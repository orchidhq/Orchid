package com.eden.orchid.presentations;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.components.OrchidComponent;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class PresentationComponent extends OrchidComponent {

    private final PresentationsModel model;

    @Getter @Setter @Option
    private String presentationKey;

    @Getter @Setter @Option @StringDefault("web-2.0")
    private String deckTheme;

    @Getter @Setter @Option @StringDefault("horizontal-slide")
    private String transitionTheme;

    @Getter
    private Presentation presentation;

    @Inject
    public PresentationComponent(OrchidContext context, PresentationsModel model) {
        super(context, "presentation", 25);
        this.model = model;
    }

    @Override
    public void addAssets() {
        super.addAssets();

        addCss("assets/core/deck.core.scss");
        addCss("assets/extensions/goto/deck.goto.scss");
        addCss("assets/extensions/menu/deck.menu.scss");
        addCss("assets/extensions/navigation/deck.navigation.scss");
        addCss("assets/extensions/status/deck.status.scss");
        addCss("assets/extensions/scale/deck.scale.scss");
        addCss("assets/themes/style/"+ deckTheme + ".scss");
        addCss("assets/themes/transition/" + transitionTheme + ".scss");

        addJs("assets/vendor/modernizr.custom.js");
        addJs("assets/core/deck.core.js");
        addJs("assets/extensions/menu/deck.menu.js");
        addJs("assets/extensions/goto/deck.goto.js");
        addJs("assets/extensions/status/deck.status.js");
        addJs("assets/extensions/navigation/deck.navigation.js");
        addJs("assets/extensions/scale/deck.scale.js");
        addJs("assets/initDeck.js");
    }

    @Override
    public void onPostExtraction() {
        this.presentation = model.getPresentations().getOrDefault(presentationKey, null);
    }
}
