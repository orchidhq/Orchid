package com.eden.orchid.api.theme.menus

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.AllOptions
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.IntDefault
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.registration.Prioritized
import com.eden.orchid.api.server.annotations.Extensible
import com.eden.orchid.api.theme.components.ModularPageListItem
import com.eden.orchid.api.theme.pages.OrchidPage

/**
 * @orchidApi extensible
 * @since v1.0.0
 */
@Extensible
@Description(value = "A factory that produces dynamic menu items.", name = "Menu Items")
abstract class OrchidMenuFactory
@JvmOverloads
constructor(
    val _type: String,
    priority: Int = 100
) : Prioritized(priority), ModularPageListItem<OrchidMenu, OrchidMenuFactory> {

    @Option("order")
    @IntDefault(0)
    @Description("By default, menu items are rendered in the order in which they are declared, but the ordering can be changed by setting the order on any individual menu item. A higher value for order will render that menu item earlier in the list.")
    var _order: Int = 0

    @Option("asSubmenu")
    @Description("Set all the menu items from this as a dropdown, instead of including them directly at the root.")
    var asSubmenu: Boolean = false

    @Option
    @Description("The title the menu")
    lateinit var submenuTitle: String

    @AllOptions
    var allData: Map<String, Any>? = null

    override fun canBeUsedOnPage(
        containingPage: OrchidPage,
        menu: OrchidMenu,
        possibleMenuItems: List<Map<String, Any>>,
        currentMenuItems: List<OrchidMenuFactory>
    ): Boolean {
        return true
    }

    override fun initialize(context: OrchidContext, containingPage: OrchidPage) {

    }

    abstract fun getMenuItems(context: OrchidContext, page: OrchidPage): List<MenuItem>

    override fun getType(): String {
        return _type
    }

    override fun setOrder(order: Int) {
        _order = order
    }

    override fun getOrder(): Int {
        return _order
    }
}
