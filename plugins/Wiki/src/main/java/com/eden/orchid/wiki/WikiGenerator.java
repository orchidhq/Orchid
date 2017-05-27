package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.utilities.OrchidUtils;
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
public class WikiGenerator extends OrchidGenerator {

    private List<WikiPage> wiki;
    public static List<JSONObject> terms = new ArrayList<>();

    private OrchidResources resources;
    private WikiPathOption option;

    private String wikiBaseDir = "wiki/";

    @Inject
    public WikiGenerator(OrchidContext context, OrchidResources resources, WikiPathOption option) {
        super(context);
        this.resources = resources;
        this.option = option;

        this.priority = 700;
        this.wiki = new ArrayList<>();
    }

    @Override
    public String getName() {
        return "wiki";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {

        if(!EdenUtils.isEmpty(option.getPath())) {
            wikiBaseDir = option.getPath();
        }

        setupSummary();
        setupGlossary();

        return wiki;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.stream().forEach((page -> page.renderTemplate()));
    }

    private void setupGlossary() {
        OrchidResource glossary = resources.getLocalResourceEntry(wikiBaseDir + "GLOSSARY.md");

        if(glossary == null) {
            return;
        }

        String content = context.getTheme().compile(glossary.getReference().getExtension(), glossary.getContent());
        Document doc = Jsoup.parse(content);

        for (Element h2 : doc.select("h2")) {
            String id = h2.text().replaceAll("\\s+", "_").toLowerCase();
            String path = wikiBaseDir + "glossary/#" + id;
            String url = OrchidUtils.applyBaseUrl(context, path);

            Element link = new Element("a");
            link.attr("href", url);
            link.text(h2.text());

            h2.attr("id", id);
            h2.empty();
            h2.appendChild(link);

            JSONObject index = new JSONObject();
            index.put("name", h2.text());
            index.put("url", url);
            terms.add(index);
        }

        String safe = doc.toString();
        glossary = new StringResource(context, wikiBaseDir + "glossary.md", safe);
        glossary.getReference().setTitle("Glossary");

        WikiPage page = new WikiPage(glossary);
        page.getReference().setUsePrettyUrl(true);
        page.setType("wiki");

        wiki.add(page);
    }

    private void setupSummary() {
        OrchidResource summary = resources.getLocalResourceEntry(wikiBaseDir + "SUMMARY.md");

        if(summary == null) {
            return;
        }

        String content = context.getTheme().compile(summary.getReference().getExtension(), summary.getContent());
        Document doc = Jsoup.parse(content);

        Elements links = doc.select("a[href]");

        WikiPage previous = null;

        for (Element a : links) {
            String file = wikiBaseDir + a.attr("href");
            String path = wikiBaseDir + FilenameUtils.removeExtension(a.attr("href"));

            OrchidResource resource = resources.getLocalResourceEntry(file);

            if(resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", new Object[]{ file });
                resource = new StringResource(context, path + File.separator + "index.md", a.text());
            }

            WikiPage page = new WikiPage(resource);

            if(!EdenUtils.isEmpty(FilenameUtils.getExtension(a.attr("href")))) {
                page.getReference().setUsePrettyUrl(true);
            }
            else {
                page.getReference().setUsePrettyUrl(true);
            }

            page.getReference().setTitle(a.text());

            page.setType("wiki");
            wiki.add(page);

            if(previous != null) {
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
        summary = new StringResource(context, wikiBaseDir + "summary.md", safe);
        summary.getReference().setTitle("Summary");

        WikiPage page = new WikiPage(summary);
        page.getReference().setUsePrettyUrl(true);

        page.setType("wiki");

        wiki.add(page);
    }
}
