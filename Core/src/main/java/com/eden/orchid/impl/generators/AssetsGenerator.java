package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AssetsGenerator extends OrchidGenerator {

    private OrchidResources resources;

    @Inject
    public AssetsGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.resources = resources;
        this.priority = 1000;
    }

    @Override
    public String getName() {
        return "assets";
    }

    @Override
    public String getDescription() {
        return "Copies and compiles all files in the 'assets' resource directory and all subdirectories according to the registered Compiler file types.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = resources.getResourceEntries("assets", null, true);
        List<OrchidPage> assets = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            assets.add(new OrchidPage(entry));
        }

        return assets;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream()
             .forEach(OrchidPage::renderRaw);
    }
}

