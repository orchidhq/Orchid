package com.eden.orchid.editorial;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;

public class EditorialTheme extends Theme {

    @Option @StringDefault("#4C376C") public String primaryColor;
    @Option @StringDefault("#000000") public String secondaryColor;

    @Option public JSONObject about;

    @Inject
    public EditorialTheme(OrchidContext context) {
        super(context, 100);
    }

    @Override
    public String getKey() {
        return "Editorial";
    }

    @Override
    protected void addAssets() {
        addCss(new OrchidPage(this.getResourceEntry("assets/css/editorial.scss"), "main_css"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/editorial.js"), "main_js"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/editorial_util.js"), "util_js"));
    }
}
