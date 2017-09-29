package com.eden.orchid.materialize;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
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
        addCss(new OrchidPage(this.getResourceEntry("assets/css/appStyles.scss"), "appStyles_scss"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/jquery-2.1.1.min.js"), "jquery-2_1_1_min_js"));
        addJs(new OrchidPage(this.getResourceEntry("assets/js/materialize.min.js"), "materialize_min_js"));

        super.addAssets();
    }
}
