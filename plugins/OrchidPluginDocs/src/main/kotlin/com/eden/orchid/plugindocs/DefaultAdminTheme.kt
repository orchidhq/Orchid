package com.eden.orchid.plugindocs

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.AdminTheme
import javax.inject.Inject

class DefaultAdminTheme @Inject
constructor(context: OrchidContext) : AdminTheme(context, "Default", 100) {

    public override fun loadAssets() {
//        addCss("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css")
//        addCss("https://cdnjs.cloudflare.com/ajax/libs/simple-line-icons/2.4.1/css/simple-line-icons.min.css")
//        addCss("http://coreui.io/docs-assets/css/style.min.css")
//        addCss("assets/css/admin_orchid.scss")
//
//        addJs("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js")
//        addJs("https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js")
//        addJs("http://coreui.io/docs-assets/js/app.js")
//        addJs("assets/js/admin_orchid.js")

        addCss("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/css/uikit.min.css")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.15/vue.min.js")
        context.getResourceEntries("assets/js/server", null, true)
                .filter  { it.reference.outputExtension.equals("js", ignoreCase = true) }
                .map     { Clog.v("${it.reference.originalPath}/${it.reference.originalFileName}.${it.reference.extension}"); it }
                .map     { "${it.reference.originalPath}/${it.reference.originalFileName}.${it.reference.extension}" }
                .forEach { addJs(it) }

        addJs("assets/js/orchid_admin.js")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/js/uikit.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/js/uikit-icons.min.js")
    }

}
