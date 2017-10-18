package com.eden.orchid.pages;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PagesGenerator extends OrchidGenerator {

    @Option("baseDir")
    @StringDefault("pages")
    public String pagesBaseDir;

    @Inject
    public PagesGenerator(OrchidContext context) {
        super(context, "pages", 700);
    }

    @Override
    public String getDescription() {
        return "Generates static pages with the same output folder as their input, minus the base directory. Input " +
                "pages come from 'baseDir' option value, which defaults to 'pages'";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidResource> resourcesList = context.getLocalResourceEntries(pagesBaseDir, null, true);
        List<StaticPage> pages = new ArrayList<>();

        for (OrchidResource entry : resourcesList) {
            entry.getReference().stripFromPath(pagesBaseDir);
            StaticPage page = new StaticPage(entry);
            pages.add(page);
        }

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        try {
            pages.forEach(context::renderTemplate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}