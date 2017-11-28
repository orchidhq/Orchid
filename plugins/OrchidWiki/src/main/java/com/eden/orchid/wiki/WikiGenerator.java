package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.annotations.Description;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.options.OptionsHolder;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;
import com.eden.orchid.wiki.model.WikiModel;
import com.eden.orchid.wiki.model.WikiSection;
import com.eden.orchid.wiki.pages.WikiPage;
import com.eden.orchid.wiki.pages.WikiSummaryPage;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Description("Create a structured and navigable knowledge-base for your project.")
public class WikiGenerator extends OrchidGenerator implements OptionsHolder {

    private final WikiModel model;

    @Option("baseDir")
    @StringDefault("wiki")
    public String wikiBaseDir;

    @Option("sections")
    public String[] sectionNames;

    @Inject
    public WikiGenerator(OrchidContext context, WikiModel model) {
        super(context, "wiki", 700);
        this.model = model;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        model.initialize();

        if (EdenUtils.isEmpty(sectionNames)) {
            WikiSection wiki = getWikiPages(null);
            if (wiki != null) {
                model.getSections().put(null, wiki);
            }
        }
        else {
            for (String section : sectionNames) {
                WikiSection wiki = getWikiPages(section);
                if (wiki != null) {
                    model.getSections().put(section, wiki);
                }
            }
        }

        return model.getAllPages();
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }

    private WikiSection getWikiPages(String section) {
        JSONObject pageMenuItem = new JSONObject();
        pageMenuItem.put("type", "wiki");

        if(!EdenUtils.isEmpty(section)) {
            pageMenuItem.put("title", section + " Wiki");
            pageMenuItem.put("section", section);
        }
        else {
            pageMenuItem.put("title", "Wiki");
        }

        ArrayList<WikiPage> wiki = new ArrayList<>();

        String sectionBaseDir = (!EdenUtils.isEmpty(section)) ?
                OrchidUtils.normalizePath(wikiBaseDir) + "/" + OrchidUtils.normalizePath(section) + "/" :
                OrchidUtils.normalizePath(wikiBaseDir) + "/";

        OrchidResource summary = context.getLocalResourceEntry(sectionBaseDir + "SUMMARY.md");

        if (summary == null) {
            Clog.w("Could not find wiki summary page in '#{}'", sectionBaseDir);
            return null;
        }

        String content = context.compile(summary.getReference().getExtension(), summary.getContent());
        Document doc = Jsoup.parse(content);

        Elements links = doc.select("a[href]");

        WikiPage previous = null;

        int i = 0;

        for (Element a : links) {
            String file = sectionBaseDir + a.attr("href");
            String path = sectionBaseDir + FilenameUtils.removeExtension(a.attr("href"));

            OrchidResource resource = context.getLocalResourceEntry(file);

            if (resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", file);
                resource = new StringResource(context, path + File.separator + "index.md", a.text());
            }

            WikiPage page = new WikiPage(resource, a.text());

            page.getMenu().addMenuItem(pageMenuItem);

            i++;
            page.setOrder(i);

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
        summaryPage.getMenu().addMenuItem(pageMenuItem);

        for(WikiPage wikiPage : wiki) {
            wikiPage.setSectionSummary(summaryPage);
        }

        return new WikiSection(section, summaryPage, wiki);
    }

}
