package com.eden.orchid.pages;


import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
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
    public String getName() {
        return "pages";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = resources.getLocalResourceEntries("pages", null, true);
        List<OrchidPage> pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            if(!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                entry.getReference().setTitle(entry.queryEmbeddedData("title").toString());
            }
            else {
                entry.getReference().setTitle(entry.getReference().getFileName());
            }

            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripBasePath("pages");
                }
            }

            entry.getReference().setUsePrettyUrl(true);

            OrchidPage page = new OrchidPage(entry);
            page.setType("page");

            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<OrchidPage> pages) {
        pages.stream()
             .forEach((page -> {
                 page.renderTemplate();
             }));
    }
}