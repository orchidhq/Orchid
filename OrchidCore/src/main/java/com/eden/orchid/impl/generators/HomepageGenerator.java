package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.components.LicenseComponent;
import com.eden.orchid.impl.components.ReadmeComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class HomepageGenerator extends OrchidGenerator {

    private OrchidResources resources;

    @Inject
    public HomepageGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.resources = resources;
        setPriority(2);
    }

    @Override
    public String getKey() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Generates the root homepage for your site.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();
        OrchidResource resource = new StringResource(context, "index.twig", "");
        OrchidPage page = new OrchidPage(resource);
        page.getReference().setTitle("Home");
        page.setType("frontPage");

        page.addComponent("readme", ReadmeComponent.class);
        page.addComponent("license", LicenseComponent.class);

        pages.add(page);
        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream().forEach((page -> page.renderTemplate()));
    }
}
