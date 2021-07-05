package com.eden.orchid.plugindocs

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.AdminTheme
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import javax.inject.Inject

@Description(value = "The default Admin Panel theme, using UiKit and Vue.js.", name = "Default")
class DefaultAdminTheme
@Inject
constructor(
    context: OrchidContext
) : AdminTheme(context, "Default") {

    override fun loadAssets(delegate: AssetManagerDelegate) {
        delegate.addCss("@uikit.css")

        delegate.addJs("@vue.js")
        context.getDefaultResourceSource(null, this).getResourceEntries(context, "assets/js/server", null, true)
            .filter { it.reference.outputExtension.equals("js", ignoreCase = true) }
            .map { resource ->
                context
                    .assetManager
                    .createAsset(
                        delegate,
                        context.getDefaultResourceSource(null, this),
                        resource.reference.originalFullFileName
                    )
            }
            .map { asset ->
                context.assetManager.getActualAsset(delegate, asset, true)
            }

        delegate.addJs("assets/js/orchid_admin.js")

        delegate.addJs("@uikit.js")
        delegate.addJs("@uikit-icons.js")
    }
}
