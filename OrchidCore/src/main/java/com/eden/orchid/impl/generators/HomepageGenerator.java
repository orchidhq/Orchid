package com.eden.orchid.impl.generators;

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

    public static final String generatorKey = "home";

    @Inject
    public HomepageGenerator(OrchidContext context) {
        super(context, generatorKey, OrchidGenerator.PRIORITY_EARLY);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();
        pages.add(getHomepage());
        return pages;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(page -> {
            if(page.getNext() == null && OrchidUtils.elementIsString(page.getAllData().query("next"))) {
                Object next = context.findInCollection(page.getAllData().query("next").toString());
                if(next != null && next instanceof OrchidPage) {
                    page.setNext((OrchidPage) next);
                }
            }
            if(page.getPrevious() == null && OrchidUtils.elementIsString(page.getAllData().query("previous"))) {
                Object previous = context.findInCollection(page.getAllData().query("previous").toString());
                if(previous != null && previous instanceof OrchidPage) {
                    page.setPrevious((OrchidPage) previous);
                }
            }
            context.renderTemplate(page);
        });
        OrchidPage _404 = get404();
        if(_404 != null) {
            context.renderTemplate(_404);
        }

        OrchidPage favicon = getFavicon();
        if(favicon != null) {
            context.renderBinary(favicon);
        }
    }

    private OrchidPage getHomepage() {
        OrchidResource resource = context.locateLocalResourceEntry("homepage");
        OrchidPage page;
        if(resource == null) {
            resource = new StringResource(context, "index.peb", "");
            page = new OrchidPage(resource, "frontPage", context.getSite().getSiteInfo().getSiteName());

            JSONObject readmeComponent = new JSONObject();
            readmeComponent.put("type", "readme");
            page.getComponents().addComponent(readmeComponent);

            JSONObject licenseComponent = new JSONObject();
            licenseComponent.put("type", "license");
            page.getComponents().addComponent(licenseComponent);
        }
        else {
            page = new OrchidPage(resource, "frontPage", context.getSite().getSiteInfo().getSiteName());
            page.getReference().setFileName("index");
        }

        page.getReference().setUsePrettyUrl(false);

        return page;
    }

    private OrchidPage get404() {
        OrchidResource resource = context.locateLocalResourceEntry("404");
        OrchidPage page = null;
        if(resource != null) {
            page = new OrchidPage(resource, "404", context.getSite().getSiteInfo().getSiteName());
            page.getReference().setFileName("404");
            page.getReference().setUsePrettyUrl(false);
        }

        return page;
    }

    private OrchidPage getFavicon() {
        OrchidResource resource = context.getResourceEntry("favicon.ico");
        OrchidPage page = null;
        if(resource != null) {
            page = new OrchidPage(resource, "favicon", context.getSite().getSiteInfo().getSiteName());
            page.getReference().setFileName("favicon");
            page.getReference().setOutputExtension("ico");
            page.getReference().setUsePrettyUrl(false);
        }

        return page;
    }
}
