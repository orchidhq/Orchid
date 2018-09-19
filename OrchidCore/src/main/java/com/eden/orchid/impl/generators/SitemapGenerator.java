package com.eden.orchid.impl.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidCollection;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.options.annotations.BooleanDefault;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class SitemapGenerator extends OrchidGenerator {

    public static final String GENERATOR_KEY = "sitemap";

    @Getter @Setter
    @Option @BooleanDefault(true)
    @Description("Whether to generate sitemaps.")
    private boolean useSitemaps;

    @Getter @Setter
    @Option @BooleanDefault(true)
    @Description("Whether to generate a robots.txt, which includes a link to the sitemap.")
    private boolean useRobots;

    @Inject
    public SitemapGenerator(OrchidContext context) {
        super(context, GENERATOR_KEY, OrchidGenerator.PRIORITY_LATE + 1);
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        generateSitemapFiles();
    }

    private void generateSitemapFiles() {
        SitemapIndexPage sitemapIndex = null;

        if(useSitemaps) {
            Map<String, OrchidIndex> mappedIndex = context.getInternalIndex().getAllIndexedPages();
            List<SitemapPage> sitemapPages = new ArrayList<>();

            // Render an page for each generator's individual index
            mappedIndex.keySet().forEach(key -> {
                List<OrchidPage> pages = mappedIndex.get(key).getAllPages();
                SitemapPage page = new SitemapPage(context, key, pages);
                sitemapPages.add(page);
                context.renderRaw(page);
            });

            // Render an index of all indices, so individual index pages can be found
            sitemapIndex = new SitemapIndexPage(context, sitemapPages);
            context.renderRaw(sitemapIndex);
        }

        if(useRobots) {
            // Render the robots.txt
            RobotsPage robotsPage = new RobotsPage(context, sitemapIndex);
            context.renderRaw(robotsPage);
        }
    }

    @Override
    public List<OrchidCollection> getCollections() {
        return null;
    }

    @Description(value = "The sitemap for a section of your site, grouped by generator.", name = "Sitemap")
    public static class SitemapPage extends OrchidPage {
        public final List<OrchidPage> entries;
        SitemapPage(OrchidContext context, String key, List<OrchidPage> entries) {
            super(context.getResourceEntry("sitemap.xml.peb"), "sitemap", "Sitemap");
            this.entries = entries;
            this.reference.setFileName("sitemap-" + key);
            this.reference.setPath("");
            this.reference.setOutputExtension("xml");
            this.reference.setUsePrettyUrl(false);
        }
    }

    @Description(value = "The root sitemap, with links to all individual sitemaps.", name = "Sitemap Index")
    public static class SitemapIndexPage extends OrchidPage {
        public final List<SitemapPage> sitemaps;
        SitemapIndexPage(OrchidContext context, List<SitemapPage> sitemaps) {
            super(context.getResourceEntry("sitemapIndex.xml.peb"), "sitemapIndex", "Sitemap Index");
            this.sitemaps = sitemaps;
            this.reference.setFileName("sitemap");
            this.reference.setPath("");
            this.reference.setOutputExtension("xml");
            this.reference.setUsePrettyUrl(false);
        }
    }

    @Description(value = "Your site's robots.txt file, for directing web crawlers.", name = "Robots.txt")
    public static class RobotsPage extends OrchidPage {
        public final SitemapIndexPage sitemap;
        RobotsPage(OrchidContext context, SitemapIndexPage sitemap) {
            super(context.getResourceEntry("robots.txt.peb"), "robots", "Robots");
            this.sitemap = sitemap;
            this.reference.setFileName("robots");
            this.reference.setPath("");
            this.reference.setOutputExtension("txt");
            this.reference.setUsePrettyUrl(false);
        }
    }
}
