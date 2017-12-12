package com.eden.orchid.editorial;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.models.About;
import com.eden.orchid.api.theme.models.Social;

import javax.inject.Inject;

public class EditorialTheme extends Theme {

    @Option @StringDefault("#f56a6a") public String primaryColor;

    @Option public About about;
    @Option public Social social;

    @Inject
    public EditorialTheme(OrchidContext context) {
        super(context, "Editorial", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/editorial_main.scss");
        addCss("assets/css/editorial_orchidCustomizations.scss");

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.min.js");
        addJs("https://cdnjs.cloudflare.com/ajax/libs/skel/3.0.1/skel.min.js");
        addJs("assets/js/editorial_util.js");
        addJs("assets/js/editorial_main.js");

        addJs("https://unpkg.com/lunr/lunr.js");
        addJs("assets/js/lunrSearch.js");

        super.addAssets();
    }

}
