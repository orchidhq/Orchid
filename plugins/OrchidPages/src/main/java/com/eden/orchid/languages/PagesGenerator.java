package com.eden.orchid.languages;


import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PagesGenerator extends OrchidGenerator {

    private OrchidResources resources;

    @Inject
    public PagesGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.resources = resources;
        this.priority = 700;
    }

    @Override
    public String getKey() {
        return "pages";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = resources.getLocalResourceEntries("pages", null, true);
        List<StaticPage> pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            if(!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                entry.getReference().setTitle(entry.queryEmbeddedData("title").toString());
            }
            else {
                entry.getReference().setTitle(entry.getReference().getFileName());
            }

            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripFromPath("pages");
                }
            }

            entry.getReference().setUsePrettyUrl(true);

            StaticPage page = new StaticPage(entry);
            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream()
             .forEach(OrchidPage::renderTemplate);
    }
}