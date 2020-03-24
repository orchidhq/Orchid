package com.eden.orchid.api.site

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.theme.components.ModularList
import com.eden.orchid.api.theme.components.ModularListItem
import com.eden.orchid.utilities.OrchidUtils

@Archetype(value = ConfigArchetype::class, key = "site.baseUrl")
abstract class BaseUrlFactory(
    private val _type: String,
    priority: Int = OrchidUtils.DEFAULT_PRIORITY
) : Prioritized(priority), ModularListItem<OrchidBaseUrls, BaseUrlFactory> {

    @Option("order")
    @IntDefault(0)
    @Description("By default, menu items are rendered in the order in which they are declared, but the ordering can be changed by setting the order on any individual menu item. A higher value for order will render that menu item earlier in the list.")
    var _order: Int = 0

    override fun getOrder(): Int {
        return _order
    }

    override fun setOrder(order: Int) {
        _order = order
    }

    override fun getType(): String = _type
    abstract fun isEnabled(context: OrchidContext): Boolean
    abstract fun getBaseUrl(context: OrchidContext): String
}

class OrchidBaseUrls : ModularList<OrchidBaseUrls, BaseUrlFactory>() {

    init {
        defaultType = "default"
    }

    override fun getItemClass(): Class<BaseUrlFactory> = BaseUrlFactory::class.java

}
