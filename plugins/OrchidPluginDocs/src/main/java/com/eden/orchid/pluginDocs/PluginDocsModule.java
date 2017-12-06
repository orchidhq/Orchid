package com.eden.orchid.pluginDocs;

import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.server.admin.AdminList;
import com.eden.orchid.pluginDocs.lists.CompilersList;
import com.eden.orchid.pluginDocs.lists.ComponentsList;
import com.eden.orchid.pluginDocs.lists.GeneratorsList;
import com.eden.orchid.pluginDocs.lists.MenuItemsList;
import com.eden.orchid.pluginDocs.lists.OptionsList;
import com.eden.orchid.pluginDocs.lists.ParsersList;
import com.eden.orchid.pluginDocs.lists.ResourceSourcesList;
import com.eden.orchid.pluginDocs.lists.TasksList;
import com.eden.orchid.pluginDocs.lists.ThemesList;

public class PluginDocsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(PluginResourceSource.class,
                PluginDocsResourceSource.class);

        addToSet(AdminList.class,
                CompilersList.class,
                ComponentsList.class,
                GeneratorsList.class,
                OptionsList.class,
                ParsersList.class,
                ResourceSourcesList.class,
                TasksList.class,
                ThemesList.class,
                MenuItemsList.class);
    }

}
