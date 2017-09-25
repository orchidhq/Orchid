package com.eden.orchid.bsdoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;

public class BSDocTheme extends Theme {

    @Inject
    public BSDocTheme(OrchidContext context) {
        super(context, "BsDoc", 100);
    }

    @Override
    protected void addAssets() {
        addCss(new OrchidPage(this.getResourceEntry("assets/css/bsdoc.scss"), "bsdoc_scss"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/bsdoc.js"), "bsdoc_js"));
    }
}
