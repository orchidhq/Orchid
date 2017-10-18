package com.eden.orchid.bsdoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.ComponentHolder;

import javax.inject.Inject;

public class BSDocTheme extends Theme {

    @Option
    @StringDefault("#4C376C")
    public String primaryColor;

    @Option
    @StringDefault("#000000")
    public String secondaryColor;

    @Option
    public About about;

    @Option
    public ComponentHolder sidebar;

    @Inject
    public BSDocTheme(OrchidContext context) {
        super(context, "BsDoc", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/bsdoc.scss");
        addJs("assets/js/bsdoc.js");

        super.addAssets();
    }

    public static class About implements OptionsHolder {

        @Option public String siteName;
        @Option public String description;

        @Option public String githubUsername;
        @Option public String githubRepo;

    }
}
