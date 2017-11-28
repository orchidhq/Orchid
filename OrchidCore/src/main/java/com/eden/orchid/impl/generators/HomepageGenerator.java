package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Description("Generates the root homepage for your site.")
public final class HomepageGenerator extends OrchidGenerator {

    @Inject
    public HomepageGenerator(OrchidContext context) {
        super(context, "home", 3);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();

        OrchidResource resource = context.getLocalResourceEntry("homepage.md");
        OrchidPage page;
        if(resource == null) {
            resource = new StringResource(context, "index.twig", "");
            page = new OrchidPage(resource, "frontPage");

            JSONObject readmeComponent = new JSONObject();
            readmeComponent.put("type", "readme");
            page.getComponents().addComponent(readmeComponent);

            JSONObject licenseComponent = new JSONObject();
            licenseComponent.put("type", "license");
            page.getComponents().addComponent(licenseComponent);
        }
        else {
            page = new OrchidPage(resource, "frontPage");
            page.getReference().setFileName("index");
        }

        page.getReference().setTitle("Home");
        page.getReference().setUsePrettyUrl(false);

        pages.add(page);

        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }
}
