package com.eden.orchid.javadoc;

import com.eden.orchid.api.compilers.TemplateFunction;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.javadoc.functions.JavadocLinkFunction;
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType;
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType;
import com.sun.javadoc.RootDoc;

@IgnoreModule
public class JavadocModule extends OrchidModule {

    private final RootDoc rootDoc;

    public JavadocModule(RootDoc rootDoc) {
        this.rootDoc = rootDoc;
    }

    @Override
    protected void configure() {
        bind(RootDoc.class).toInstance(rootDoc);

        addToSet(OrchidGenerator.class,
                JavadocGenerator.class);

        addToSet(PluginResourceSource.class,
                JavadocResourceSource.class);

        addToSet(OrchidMenuItem.class,
                AllClassesMenuItemType.class,
                AllPackagesMenuItemType.class);

        addToSet(TemplateFunction.class,
                JavadocLinkFunction.class);
    }
}
