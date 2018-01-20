package com.eden.orchid.pluginDocs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.AdminTheme
import javax.inject.Inject

class DefaultAdminTheme @Inject
constructor(context: OrchidContext) : AdminTheme(context, "Default", 100) {

    public override fun loadAssets() {
        addCss("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
        addCss("https://cdnjs.cloudflare.com/ajax/libs/simple-line-icons/2.4.1/css/simple-line-icons.min.css")
        addCss("http://coreui.io/docs-assets/css/style.min.css")
        addCss("assets/css/admin_orchid.css")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.13.0/popper.min.js.map")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0-beta.3/js/bootstrap.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/pace/1.0.2/pace.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.0/Chart.min.js")
        addJs("http://coreui.io/docs-assets/js/app.js")
        addJs("assets/js/admin_orchid.js")
    }

}
