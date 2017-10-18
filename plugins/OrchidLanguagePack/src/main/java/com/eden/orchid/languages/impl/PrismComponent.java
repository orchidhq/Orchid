package com.eden.orchid.languages.impl;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class PrismComponent extends OrchidComponent {

    @Getter @Setter @Option
    @StringDefault("https://cdnjs.cloudflare.com/ajax/libs/prism/1.8.1")
    private String prismSource;

    @Getter @Setter @Option private String[] languages;

    @Getter @Setter @Option
    private String theme;

    @Getter @Setter @Option
    private String githubTheme;

    @Getter @Setter @Option private boolean scriptsOnly;

    @Inject
    public PrismComponent(OrchidContext context) {
        super(context, "prism", 100);
    }

    @Override public void addAssets() {
        super.addAssets();

        addJs(OrchidUtils.normalizePath(prismSource) + "/prism.min.js");

        if(!scriptsOnly) {
            if(!EdenUtils.isEmpty(theme)) {
                addCss(OrchidUtils.normalizePath(prismSource) + "/themes/prism-" + theme + ".min.css");
            }
            else if(!EdenUtils.isEmpty(githubTheme)) {
                addCss("https://raw.githubusercontent.com/PrismJS/prism-themes/master/themes/prism-" + githubTheme + ".css");
            }
            else {
                addCss(OrchidUtils.normalizePath(prismSource) + "/themes/prism.min.css");
            }
        }

        if(!EdenUtils.isEmpty(languages)) {
            for(String lang : languages) {
                addJs(OrchidUtils.normalizePath(prismSource) + "/components/prism-" + lang + ".min.js");
            }
        }
    }
}
