package com.eden.orchid.impl.themes;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.AdminTheme;

import javax.inject.Inject;

public final class DefaultAdminTheme extends AdminTheme {

    @Inject
    public DefaultAdminTheme(OrchidContext context) {
        super(context, "Default", 100);
    }

    @Override
    public void addAssets() {
        addCss("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css");
        addCss("https://cdnjs.cloudflare.com/ajax/libs/simple-line-icons/2.4.1/css/simple-line-icons.min.css");

        addCss("assets/css/admin_style.css");
        addCss("assets/css/admin_orchid.css");

        addJs("assets/js/vendor/jquery.min.js");
        addJs("assets/js/vendor/popper.min.js");
        addJs("assets/js/vendor/bootstrap.min.js");
        addJs("assets/js/vendor/pace.min.js");
        addJs("assets/js/vendor/Chart.min.js");

        addJs("assets/js/admin_app.js");
        addJs("assets/js/admin_orchid.js");

        super.addAssets();
    }

}
