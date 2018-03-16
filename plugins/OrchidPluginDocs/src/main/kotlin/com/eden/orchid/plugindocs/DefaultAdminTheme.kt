package com.eden.orchid.plugindocs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.AdminTheme
import javax.inject.Inject

class DefaultAdminTheme @Inject
constructor(context: OrchidContext) : AdminTheme(context, "Default", 100) {

    public override fun loadAssets() {
        addCss("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/css/uikit.min.css")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.15/vue.min.js")
        context.getResourceEntries("assets/js/server", null, true)
                .filter  { it.reference.outputExtension.equals("js", ignoreCase = true) }
                .map     { "${it.reference.originalPath}/${it.reference.originalFileName}.${it.reference.extension}" }
                .forEach { addJs(it) }

        addJs("assets/js/orchid_admin.js")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/js/uikit.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-beta.40/js/uikit-icons.min.js")
    }

}
