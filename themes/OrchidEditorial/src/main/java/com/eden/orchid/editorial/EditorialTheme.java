package com.eden.orchid.editorial;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.theme.Theme;
import org.json.JSONObject;

import javax.inject.Inject;

public class EditorialTheme extends Theme {

    @Option @StringDefault("#4C376C") public String primaryColor;
    @Option @StringDefault("#000000") public String secondaryColor;

    @Option public JSONObject about;

    @Inject
    public EditorialTheme(OrchidContext context) {
        super(context, "Editorial", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/editorial.scss");
        addJs("assets/js/editorial.js");
        addJs("assets/js/editorial_util.js");

        super.addAssets();
    }
}
