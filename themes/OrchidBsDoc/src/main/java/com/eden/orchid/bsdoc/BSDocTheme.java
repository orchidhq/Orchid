package com.eden.orchid.bsdoc;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.models.About;
import com.eden.orchid.api.theme.models.Social;

import javax.inject.Inject;

public class BSDocTheme extends Theme {

    @Option @StringDefault("#4C376C") public String primaryColor;
    @Option @StringDefault("#000000") public String secondaryColor;

    @Option public About about;
    @Option public Social social;

    @Option
    public ComponentHolder sidebar;

    @Inject
    public BSDocTheme(OrchidContext context) {
        super(context, "BsDoc", 100);
    }

    @Override
    public void addAssets() {
        addCss("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css");
        addCss("https://cdnjs.cloudflare.com/ajax/libs/github-fork-ribbon-css/0.2.0/gh-fork-ribbon.min.css");
        addCss("assets/css/bsdoc.scss");

        addJs("https://code.jquery.com/jquery-1.11.2.min.js");
        addJs("https://netdna.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js");
        addJs("https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.3.0/bootbox.min.js");
        addJs("assets/js/bsdoc.js");

        super.addAssets();
    }

}
