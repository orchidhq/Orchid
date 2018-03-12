package com.eden.orchid.api.theme.menus.menuItem;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.menus.OrchidMenu;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;

import java.util.List;

/**
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
public abstract class OrchidMenuItem extends Prioritized implements OptionsHolder {

    protected final OrchidContext context;

    @Getter protected final String key;

    @Setter protected OrchidPage page;

    public OrchidMenuItem(OrchidContext context, String key, int priority) {
        super(priority);
        this.key = key;
        this.context = context;
    }

    public boolean canBeUsedOnPage(
            OrchidPage containingPage,
            OrchidMenu menu,
            JSONArray possibleMenuItems,
            List<OrchidMenuItem> currentMenuItems) {
        return true;
    }

    public abstract List<OrchidMenuItemImpl> getMenuItems();

}
