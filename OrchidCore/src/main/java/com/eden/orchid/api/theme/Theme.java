package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Archetype;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.archetypes.ConfigArchetype;
import com.eden.orchid.api.server.annotations.ImportantModularType;
import com.eden.orchid.api.theme.components.ComponentHolder;
import com.eden.orchid.api.theme.components.MetaComponentHolder;
import com.eden.orchid.api.theme.menus.OrchidMenu;

import javax.inject.Inject;

import static com.eden.orchid.utilities.OrchidUtils.DEFAULT_PRIORITY;

@ImportantModularType
@Description(value = "A collection of assets and templates used to render your site.", name = "Themes")
@Archetype(value = ConfigArchetype.class, key = "theme")
public abstract class Theme extends AbstractTheme {

    @Option
    @Description("The primary menu for your site. Different themes may specify additional menus.")
    protected OrchidMenu menu;

    @Option
    @Description("The components that comprise the meta-info for all pages using this theme. Typically extra scripts or" +
            " meta tags included in the `HEAD` of a page."
    )
    protected MetaComponentHolder metaComponents;

    @Inject
    public Theme(OrchidContext context, String key, int priority) {
        super(context, key, priority);
    }

    @Inject
    public Theme(OrchidContext context, String key) {
        super(context, key, DEFAULT_PRIORITY);
    }

    public OrchidMenu getMenu() {
        return this.menu;
    }

    public void setMenu(OrchidMenu menu) {
        this.menu = menu;
    }

    @Override
    protected ComponentHolder[] getComponentHolders() {
        return new ComponentHolder[] { metaComponents };
    }

    public MetaComponentHolder getMetaComponents() {
        return metaComponents;
    }

    public void setMetaComponents(MetaComponentHolder metaComponents) {
        this.metaComponents = metaComponents;
    }
}
