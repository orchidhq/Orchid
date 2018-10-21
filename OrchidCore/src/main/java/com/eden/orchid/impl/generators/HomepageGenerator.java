package com.eden.orchid.impl.generators;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Description(value = "Generates the root homepage for your site.", name = "Homepage")
public final class HomepageGenerator extends OrchidGenerator {

    public static final String GENERATOR_KEY = "home";

    private Homepage homepage;

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
        if (favicon != null) {
            context.renderBinary(favicon);
        }
    }

    @Override
    public List<? extends OrchidCollection> getCollections() {
        return Collections.singletonList(new HomepageCollection(this));
    }

    private Homepage getHomepage() {
        if(this.homepage == null) {
            OrchidResource resource = context.locateLocalResourceEntry("homepage");
            if (resource == null) {
                resource = new StringResource(context, "homepage.md", "");
            }

            homepage = new Homepage(resource, "frontPage", context.getSite().getSiteInfo().getSiteName());
            homepage.getReference().setFileName("");
        }

        return homepage;
    }

    private OrchidPage get404() {
        OrchidResource resource = context.locateLocalResourceEntry("404");
        OrchidPage page = null;
        if (resource != null) {
            page = new OrchidPage(resource, "404", context.getSite().getSiteInfo().getSiteName());
            page.getReference().setFileName("404");
            page.getReference().setUsePrettyUrl(false);
        }

        return page;
    }

    private OrchidPage getFavicon() {
        OrchidResource resource = context.getResourceEntry("favicon.ico");
        OrchidPage page = null;
        if (resource != null) {
            page = new OrchidPage(resource, "favicon", context.getSite().getSiteInfo().getSiteName());
            page.getReference().setFileName("favicon");
            page.getReference().setOutputExtension("ico");
            page.getReference().setUsePrettyUrl(false);
        }

        return page;
    }

    @Description(value = "The root page for your entire site.", name = "Homepage")
    public static class Homepage extends OrchidPage {

        public Homepage(OrchidResource resource, String key, String title) {
            super(resource, key, title);
        }

    }

    public static class HomepageCollection extends OrchidCollection<OrchidPage> {

        public HomepageCollection(HomepageGenerator homepageGenerator) {
            super(homepageGenerator, "home", Collections.singletonList(homepageGenerator.homepage));
        }

        @Override
        protected Stream<OrchidPage> find(String id) {
            if(id.equalsIgnoreCase("home") && !EdenUtils.isEmpty(this.getItems())) {
                return Stream.of(this.getItems().get(0));
            }

            return null;
        }
    }

}
