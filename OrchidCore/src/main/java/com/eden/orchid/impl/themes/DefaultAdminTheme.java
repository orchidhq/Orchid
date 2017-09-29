package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.AdminTheme;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.List;

public final class DefaultAdminTheme extends AdminTheme {

    @Inject
    public DefaultAdminTheme(OrchidContext context) {
        super(context, "Default", 100);
    }

    @Override
    public void addAssets() {
        addCss("assets/css/admin_style.css");

        addJs("assets/js/vendor/jquery.min.js");
        addJs("assets/js/vendor/popper.min.js");
        addJs("assets/js/vendor/bootstrap.min.js");
        addJs("assets/js/vendor/pace.min.js");
        addJs("assets/js/vendor/Chart.min.js");

        addJs("assets/js/admin_app.js");

        super.addAssets();
    }

    // TODO: Remove these methods when Jtwig has updated to access default methods
    @Override
    public List<OrchidPage> getScripts() {
        return getAssetHolder().getScripts();
    }

    @Override
    public List<OrchidPage> getStyles() {
        return getAssetHolder().getStyles();
    }

}
