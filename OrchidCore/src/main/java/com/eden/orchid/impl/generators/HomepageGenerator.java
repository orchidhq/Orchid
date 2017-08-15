package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.themes.components.ReadmeComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class HomepageGenerator extends OrchidGenerator {

    @Inject
    public HomepageGenerator(OrchidContext context, OrchidRenderer renderer) {
        super(2, "home", context, renderer);
    }

    @Override
    public String getDescription() {
        return "Generates the root homepage for your site.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();
        OrchidResource resource = new StringResource(context, "index.twig", "");
        OrchidPage page = new OrchidPage(resource, "frontPage");
        page.getReference().setTitle("Home");
        page.getReference().setUsePrettyUrl(false);
        page.addComponent(ReadmeComponent.class);
        pages.add(page);
        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(renderer::renderTemplate);
    }
}
