package com.eden.orchid.writersblocks.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.ValidationError
import com.eden.orchid.api.options.annotations.BooleanDefault
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.validateNotBlank
import com.eden.orchid.api.theme.assets.AssetManagerDelegate
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.utilities.OrchidUtils

@Description(
    "Add the Google Tag Manager script to your site. By default, it will only be added when building in a " +
        "production environment."
)
class GoogleTagManagerComponent : OrchidComponent("googleTagManager", true, MetaLocation.bodyStart, OrchidUtils.DEFAULT_PRIORITY) {

    @Option
    @Description("Your site's containerId")
    lateinit var containerId: String

    @Option
    @Description("If true, the script will only be added to your site in production builds")
    @BooleanDefault(true)
    var productionOnly: Boolean = true

    override fun validate(context: OrchidContext): List<ValidationError> {
        return super.validate(context) + listOfNotNull(
            validateNotBlank("containerId", containerId)
        )
    }

    private val shouldAdd: Boolean get() = !productionOnly || (productionOnly && context.isProduction)

    override fun loadAssets(delegate: AssetManagerDelegate): Unit = with(delegate) {
        if (shouldAdd) {
            addJs("assets/js/googleTagManager.js") {
                inlined = true
            }
        }
    }

    override fun isHidden(): Boolean {
        return !shouldAdd
    }
}
