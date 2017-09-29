package com.eden.orchid.html5up.futureimperfect;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.ApplyBaseUrl;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.menus.OrchidMenu;

import javax.inject.Inject;

public class FutureImperfectTheme extends Theme {

    @Option public About about;
    @Option public Social social;

    @Option public OrchidMenu topMenu;
    @Option public OrchidMenu drawerMenu;

    @Option public ComponentHolder sidebar;

    @Inject
    public FutureImperfectTheme(OrchidContext context) {
        super(context, "FutureImperfect", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/main.css");
        addCss("assets/css/orchidCustomizations.scss");

        addJs("assets/js/jquery.min.js");
        addJs("assets/js/skel.min.js");
        addJs("assets/js/util.js");
        addJs("assets/js/main.js");

        super.addAssets();
    }

    public static class About implements OptionsHolder {

        @Option public String siteName;
        @Option public String tagline;
        @Option public String blurb;

        @Option
        @ApplyBaseUrl
        public String avatar;

    }

    public static class Social implements OptionsHolder {

        @Option public String twitter;
        @Option public String facebook;
        @Option public String instagram;
        @Option public String rss;
        @Option public String email;

    }
}
