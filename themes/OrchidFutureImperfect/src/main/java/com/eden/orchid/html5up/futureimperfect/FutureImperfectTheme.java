package com.eden.orchid.html5up.futureimperfect;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class FutureImperfectTheme extends Theme {

    @Getter @Setter @Option protected ComponentHolder sidebar;

    @Inject
    public FutureImperfectTheme(OrchidContext context) {
        super(context, "FutureImperfect", 100);
    }

    @Override
    public void addAssets() {
        addCss(new OrchidPage(this.getResourceEntry("assets/css/main.scss"), "main_css"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/jquery.min.js"), "main_js"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/util.js"), "util_js"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/main.js"), "util_js"));
    }

}
