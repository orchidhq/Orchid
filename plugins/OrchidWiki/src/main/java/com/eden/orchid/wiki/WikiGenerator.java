package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.render.OrchidRenderer;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class WikiGenerator extends OrchidGenerator implements OptionsHolder {

    public static Map<String, EdenPair<WikiSummaryPage, List<WikiPage>>> sections;

    @Option("baseDir")
    @StringDefault("wiki")
    public String wikiBaseDir;

    @Option("sections")
    public String[] sectionNames;

    @Inject
    public WikiGenerator(OrchidContext context, OrchidResources resources, OrchidRenderer renderer) {
        super(700, "wiki", context, resources, renderer);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        sections = new HashMap<>();

        if (EdenUtils.isEmpty(sectionNames)) {
            EdenPair<WikiSummaryPage, List<WikiPage>> wiki = getWikiPages(null);
            if (wiki != null) {
                sections.put(null, wiki);
            }
        }
        else {
            for (String section : sectionNames) {
                EdenPair<WikiSummaryPage, List<WikiPage>> wiki = getWikiPages(section);
                if (wiki != null) {
                    sections.put(section, wiki);
                }
            }
        }

        List<OrchidPage> allPages = new ArrayList<>();
        for (String key : sections.keySet()) {
            allPages.add(sections.get(key).first);
            allPages.addAll(sections.get(key).second);
        }

        return allPages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(renderer::renderTemplate);
    }

    private EdenPair<WikiSummaryPage, List<WikiPage>> getWikiPages(String section) {
        ArrayList<WikiPage> wiki = new ArrayList<>();

        String sectionBaseDir = (!EdenUtils.isEmpty(section)) ?
                OrchidUtils.normalizePath(wikiBaseDir) + "/" + OrchidUtils.normalizePath(section) + "/" :
                OrchidUtils.normalizePath(wikiBaseDir) + "/";

        OrchidResource summary = resources.getLocalResourceEntry(sectionBaseDir + "SUMMARY.md");

        Clog.i("Wiki Base Dir: {}. Section: {}. Section Base Dir: {}", wikiBaseDir, section, sectionBaseDir);

        if (summary == null) {
            Clog.w("Could not find wiki summary page in '#{$1}'", sectionBaseDir);
            return null;
        }

        String content = context.compile(summary.getReference().getExtension(), summary.getContent());
        Document doc = Jsoup.parse(content);

        Elements links = doc.select("a[href]");

        WikiPage previous = null;

        int i = 1;

        for (Element a : links) {
            String file = sectionBaseDir + a.attr("href");
            String path = sectionBaseDir + FilenameUtils.removeExtension(a.attr("href"));

            OrchidResource resource = resources.getLocalResourceEntry(file);

            if (resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", file);
                resource = new StringResource(context, path + File.separator + "index.md", a.text());
            }

            WikiPage page = new WikiPage(resource, a.text());

            page.setOrder(i);
            i++;

            wiki.add(page);

            if (previous != null) {
                previous.setNext(page);
                page.setPrevious(previous);

                previous = page;
            }
            else {
                previous = page;
            }

            a.attr("href", page.getReference().toString());
        }

        String safe = doc.toString();
        summary = new StringResource(context, sectionBaseDir + "summary.md", safe);

        String sectionTitle = (!EdenUtils.isEmpty(section)) ? section : "Wiki";
        WikiSummaryPage summaryPage = new WikiSummaryPage(summary, OrchidUtils.camelcaseToTitleCase(sectionTitle));
        summaryPage.getReference().setUsePrettyUrl(true);

        return new EdenPair<>(summaryPage, wiki);
    }
}
