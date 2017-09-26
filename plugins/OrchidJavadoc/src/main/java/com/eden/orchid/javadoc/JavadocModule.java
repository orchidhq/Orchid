package com.eden.orchid.javadoc;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.registration.IgnoreModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType;
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.eden.orchid.javadoc.tags.api.JavadocInlineTagHandler;
import com.eden.orchid.javadoc.tags.impl.AuthorTag;
import com.eden.orchid.javadoc.tags.impl.DeprecatedTag;
import com.eden.orchid.javadoc.tags.impl.ExceptionTag;
import com.eden.orchid.javadoc.tags.impl.LinkTag;
import com.eden.orchid.javadoc.tags.impl.ReturnTag;
import com.eden.orchid.javadoc.tags.impl.SeeTag;
import com.eden.orchid.javadoc.tags.impl.SinceTag;
import com.eden.orchid.javadoc.tags.impl.ThrowsTag;
import com.eden.orchid.javadoc.tags.impl.VersionTag;

@IgnoreModule
public class JavadocModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class,
                JavadocGenerator.class);

        // Block Tag Handlers
        addToSet(JavadocBlockTagHandler.class,
                AuthorTag.class,
                DeprecatedTag.class,
                ExceptionTag.class,
                ReturnTag.class,
                SeeTag.class,
                SinceTag.class,
                ThrowsTag.class,
                VersionTag.class);

        // Inline Tag Handlers
        addToSet(JavadocInlineTagHandler.class,
                LinkTag.class);

        addToSet(PluginResourceSource.class,
                JavadocResourceSource.class);

        addToSet(OrchidMenuItem.class,
                AllClassesMenuItemType.class,
                AllPackagesMenuItemType.class);
    }
}
