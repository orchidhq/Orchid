package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.List;

public class DefaultAdminTheme extends AdminTheme {

    @Inject
    public DefaultAdminTheme(OrchidContext context) {
        super(context, "Default");
    }

    @Override
    protected void addAssets() {
        addCss("assets/css/admin_style.css");

        addJs("assets/js/vendor/jquery.min.js");
        addJs("assets/js/vendor/popper.min.js");
        addJs("assets/js/vendor/bootstrap.min.js");
        addJs("assets/js/vendor/pace.min.js");
        addJs("assets/js/vendor/Chart.min.js");

        addJs("assets/js/admin_app.js");
    }

    @Override
    public List<OrchidPage> getScripts() {
        return getAssetHolder().getScripts();
    }

    @Override
    public List<OrchidPage> getStyles() {
        return getAssetHolder().getStyles();
    }
}
