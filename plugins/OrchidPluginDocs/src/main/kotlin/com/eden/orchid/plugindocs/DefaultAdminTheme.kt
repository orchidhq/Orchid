package com.eden.orchid.plugindocs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.theme.Theme
import javax.inject.Inject

@Description(value = "The default Admin Panel theme, using UiKit and Vue.js.", name = "Default")
@Archetype(value = ConfigArchetype::class, key = "adminTheme")
class DefaultAdminTheme
@Inject
constructor(
    context: OrchidContext
) : Theme(context, "Admin") {

    public override fun loadAssets() {
        addCss("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/css/uikit.min.css")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.17/vue.min.js")
        context.getResourceEntries("assets/js/server", null, true, null)
            .filter { it.reference.outputExtension.equals("js", ignoreCase = true) }
            .map { "${it.reference.originalPath}/${it.reference.originalFileName}.${it.reference.extension}" }
            .forEach { addJs(it) }

        addJs("assets/js/orchid_admin.js")

        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/js/uikit.min.js")
        addJs("https://cdnjs.cloudflare.com/ajax/libs/uikit/3.0.0-rc.16/js/uikit-icons.min.js")
    }

}
