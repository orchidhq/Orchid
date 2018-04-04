package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.impl.relations.PageRelation;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Description("Generates the root homepage for your site.")
public final class HomepageGenerator extends OrchidGenerator {

    public static final String GENERATOR_KEY = "home";

    @Inject
    public HomepageGenerator(OrchidContext context) {
        super(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_EARLY);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        List<OrchidPage> pages = new ArrayList<>();
        pages.add(getHomepage());
        pages.add(get404());
        return pages;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);

        OrchidPage favicon = getFavicon();
        if(favicon != null) {
            context.renderBinary(favicon);
        }
    }

    private OrchidPage getHomepage() {
        OrchidResource resource = context.locateLocalResourceEntry("homepage");
        Homepage page;
        if(resource == null) {
            resource = new StringResource(context, "index.peb", "");
            page = new Homepage(resource, "frontPage", context.getSite().getSiteInfo().getSiteName());

            JSONObject readmeComponent = new JSONObject();
            readmeComponent.put("type", "readme");
            page.getComponents().add(readmeComponent);

            JSONObject licenseComponent = new JSONObject();
            licenseComponent.put("type", "license");
            page.getComponents().add(licenseComponent);
        }
        else {
            page = new Homepage(resource, "frontPage", context.getSite().getSiteInfo().getSiteName());
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

    public static class Homepage extends OrchidPage {

        @Option("next")
        @Description("The reference to the next page to link to.")
        public PageRelation nextPage;

        @Option("previous")
        @Description("The reference to the previous page to link to.")
        public PageRelation previousPage;

        public Homepage(OrchidResource resource, String key, String title) {
            super(resource, key, title);
        }

        @Override
        public OrchidPage getNext() {
            if(nextPage != null && nextPage.get() != null) {
                return nextPage.get();
            }

            return super.getNext();
        }

        @Override
        public OrchidPage getPrevious() {
            if(previousPage != null && previousPage.get() != null) {
                return previousPage.get();
            }

            return super.getPrevious();
        }
    }
}
