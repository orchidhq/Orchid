package com.eden.orchid.pages;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PagesGenerator extends OrchidGenerator {

    @Inject
    public PagesGenerator(OrchidContext context, OrchidRenderer renderer) {
        super(700, "pages", context, renderer);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = context.getLocalResourceEntries("pages", null, true);
        List<StaticPage> pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripFromPath("pages");
                }
            }

            StaticPage page = new StaticPage(entry);
            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        try {
            pages.forEach(renderer::renderTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}