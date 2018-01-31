package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Description("Generates the root homepage for your site.")
public final class HomepageGenerator extends OrchidGenerator {

    @Inject
    public HomepageGenerator(OrchidContext context) {
        super(context, "home", OrchidGenerator.PRIORITY_EARLY);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();

        OrchidResource resource = context.getLocalResourceEntry("homepage.md");
        OrchidPage page;
        if(resource == null) {
            resource = new StringResource(context, "index.peb", "");
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
            
            if(page.getNext() == null && OrchidUtils.elementIsString(page.getAllData().query("next"))) {
                List<OrchidPage> next = context.getIndex().find(page.getAllData().query("next").toString());
                if(!EdenUtils.isEmpty(next)) {
                    page.setNext(next.get(0));
                }
            }
            if(page.getPrevious() == null && OrchidUtils.elementIsString(page.getAllData().query("previous"))) {
                List<OrchidPage> previous = context.getIndex().find(page.getAllData().query("previous").toString());
                if(!EdenUtils.isEmpty(previous)) {
                    page.setPrevious(previous.get(0));
                }
            }
        }

        if(page.getTitle().equals("homepage")) {
            page.setTitle(context.getSite().getSiteInfo().getSiteName());
        }
        page.getReference().setUsePrettyUrl(false);

        pages.add(page);

        return pages;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }
}
