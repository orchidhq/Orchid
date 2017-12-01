package com.eden.orchid.materialize;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.Theme;
import org.json.JSONObject;

import javax.inject.Inject;

public class MaterializeTheme extends Theme {

    @Option
    public JSONObject colors;

    @Option
    public JSONObject shades;

    @Inject
    public MaterializeTheme(OrchidContext context) {
        super(context, "Materialize", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/appStyles.scss");
        addJs("assets/js/jquery-2.1.1.min.js");
        addJs("assets/js/materialize.min.js");

        super.addAssets();
    }
}
