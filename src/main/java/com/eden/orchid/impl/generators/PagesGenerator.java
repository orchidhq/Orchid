package com.eden.orchid.impl.generators;


import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PagesGenerator implements OrchidGenerator {

    private List<OrchidPage> pages;
    private OrchidResources resources;

    @Inject
    public PagesGenerator(OrchidResources resources) {
        this.resources = resources;
    }

    @Override
    public int priority() {
        return 700;
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
    public JSONElement startIndexing() {
        JSONObject sitePages = new JSONObject();

        List<OrchidResource> resourcesList = resources.getResourceDirEntries("pages", null, true);
        pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            if(!EdenUtils.isEmpty(entry.queryEmbeddedData("title"))) {
                entry.getReference().setTitle(entry.queryEmbeddedData("title").toString());
            }
            else {
                entry.getReference().setTitle(entry.getReference().getFileName());
            }

            if(entry.queryEmbeddedData("root") != null) {
                if(Boolean.parseBoolean(entry.queryEmbeddedData("root").toString())) {
                    entry.getReference().stripBasePath("pages/");
                }
            }

            entry.getReference().setUsePrettyUrl(true);

            OrchidPage page = new OrchidPage(entry);

            pages.add(page);

            JSONObject index = new JSONObject();
            index.put("name", page.getReference().getTitle());
            index.put("title", page.getReference().getTitle());
            index.put("url", page.getReference().toString());

            OrchidUtils.buildTaxonomy(entry, sitePages, index);
        }

        return new JSONElement(sitePages);
    }

    @Override
    public void startGeneration() {
        for (OrchidPage page : pages) {
            page.renderTemplate("templates/pages/page.twig");
        }
    }
}
