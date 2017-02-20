package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class HomepageGenerator extends OrchidGenerator {

    private OrchidContext context;
    private OrchidResources resources;

    @Inject
    public HomepageGenerator(OrchidContext context, OrchidResources resources) {
        this.context = context;
        this.resources = resources;

        setPriority(1);
    }

    @Override
    public String getName() {
        return "home";
    }

    @Override
    public String getDescription() {
        return "Generates the root homepage for your site.";
    }

    @Override
    public List<OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();

        JSONObject frontPageData = new JSONObject(getContext().getRoot().toMap());
        OrchidResource readmeResource = resources.getProjectReadme();
        if (readmeResource != null) {
            frontPageData.put("readme", context.getTheme().compile(readmeResource.getReference().getExtension(), readmeResource.getContent()));
        }

        OrchidResource licenseResource = resources.getProjectLicense();
        if (licenseResource != null) {
            frontPageData.put("license", context.getTheme().compile(licenseResource.getReference().getExtension(), licenseResource.getContent()));
        }

        OrchidResource resource = new StringResource("index.twig", "");
        OrchidPage page = new OrchidPage(resource);

        page.setData(frontPageData);
        page.getReference().setTitle("Home");

        pages.add(page);

        return pages;
    }

    @Override
    public void startGeneration(List<OrchidPage> pages) {
        for(OrchidPage page : pages) {
            page.renderTemplate("templates/pages/frontPage.twig");
        }
    }
}
