package com.eden.orchid.javadoc

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.IgnoreModule
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.javadoc.functions.JavadocLinkFunction
import com.eden.orchid.javadoc.functions.JavadocVisibilityFunction
import com.eden.orchid.javadoc.menu.AllClassesMenuItemType
import com.eden.orchid.javadoc.menu.AllPackagesMenuItemType
import com.sun.javadoc.RootDoc

@IgnoreModule
class JavadocModule(private val rootDoc: RootDoc) : OrchidModule() {

    override fun configure() {
        bind(RootDoc::class.java).toInstance(rootDoc)

        addToSet(OrchidGenerator::class.java,
                JavadocGenerator::class.java)

        addToSet(PluginResourceSource::class.java,
                JavadocResourceSource::class.java)

        addToSet(OrchidMenuItem::class.java,
                AllClassesMenuItemType::class.java,
                AllPackagesMenuItemType::class.java)

        addToSet(TemplateFunction::class.java,
                JavadocLinkFunction::class.java,
                JavadocVisibilityFunction::class.java)
    }

}
