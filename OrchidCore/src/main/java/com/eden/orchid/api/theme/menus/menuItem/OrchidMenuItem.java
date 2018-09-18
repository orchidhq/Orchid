package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.IntDefault;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.components.ModularPageListItem;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.Map;

/**
 * @orchidApi extensible
 * @since v1.0.0
 */
@Extensible
@Description(value = "A factory that produces dynamic menu items.", name = "Menu Items")
public abstract class OrchidMenuItem extends Prioritized implements ModularPageListItem<OrchidMenu, OrchidMenuItem> {

    protected final OrchidContext context;

    protected final String type;

    protected OrchidPage page;

    @Option
    @IntDefault(0)
    @Description("By default, menu items are rendered in the order in which they are declared, but the ordering can " +
            "be changed by setting the order on any individual menu item. A higher value for order will render that " +
            "menu item earlier in the list."
    )
    protected int order;

    public OrchidMenuItem(OrchidContext context, String type, int priority) {
        super(priority);
        this.type = type;
        this.context = context;
    }

    public boolean canBeUsedOnPage(
            OrchidPage containingPage,
            OrchidMenu menu,
            List<Map<String, Object>> possibleMenuItems,
            List<OrchidMenuItem> currentMenuItems) {
        return true;
    }

    public abstract List<OrchidMenuItemImpl> getMenuItems();

    public String getType() {
        return this.type;
    }

    public int getOrder() {
        return this.order;
    }

    public void setPage(OrchidPage page) {
        this.page = page;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
