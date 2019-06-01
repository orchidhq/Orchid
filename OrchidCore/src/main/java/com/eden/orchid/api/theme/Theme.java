package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ThemeConfigArchetype;
import com.eden.orchid.api.server.annotations.Extensible;
import com.eden.orchid.api.theme.menus.OrchidMenu;

import javax.inject.Inject;

/**
 *
 * @since v1.0.0
 * @orchidApi extensible
 */
@Extensible
@Description(value = "A collection of assets and templates used to render your site.", name = "Themes")
@Archetype(value = ThemeConfigArchetype.class, key = "theme")
public abstract class Theme extends AbstractTheme {

    @Option
    @Description("The primary menu for your site. Different themes may specify additional menus.")
    protected OrchidMenu menu;

    @Inject
    public Theme(OrchidContext context, String key, int priority) {
        super(context, key, priority);
    }

    public OrchidMenu getMenu() {
        return this.menu;
    }

    public void setMenu(OrchidMenu menu) {
        this.menu = menu;
    }
}
