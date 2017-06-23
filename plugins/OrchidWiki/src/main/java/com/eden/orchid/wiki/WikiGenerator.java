package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.indexing.OrchidIndex;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.menus.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.api.theme.pages.OrchidPage;
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
import java.util.Map;

@Singleton
public class WikiGenerator extends OrchidGenerator implements OrchidMenuItemType {

    private List<WikiPage> wiki;
    public static List<JSONObject> terms = new ArrayList<>();

    private OrchidResources resources;

    private String wikiBaseDir = "wiki/";

    @Inject
    public WikiGenerator(OrchidContext context, OrchidResources resources) {
        super(context);
        this.resources = resources;

        this.priority = 700;
        this.wiki = new ArrayList<>();
    }

    @Override
    public String getKey() {
        return "wiki";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
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

        if (glossary == null) {
            return;
        }

        String content = context.compile(glossary.getReference().getExtension(), glossary.getContent());
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

        if (summary == null) {
            return;
        }

        String content = context.compile(summary.getReference().getExtension(), summary.getContent());
        Document doc = Jsoup.parse(content);

        Elements links = doc.select("a[href]");

        WikiPage previous = null;

        int i = 1;

        for (Element a : links) {
            String file = wikiBaseDir + a.attr("href");
            String path = wikiBaseDir + FilenameUtils.removeExtension(a.attr("href"));

            OrchidResource resource = resources.getLocalResourceEntry(file);

            if (resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", new Object[]{file});
                resource = new StringResource(context, path + File.separator + "index.md", a.text());
            }

            WikiPage page = new WikiPage(resource);
            page.setOrder(i);
            i++;

            if (!EdenUtils.isEmpty(FilenameUtils.getExtension(a.attr("href")))) {
                page.getReference().setUsePrettyUrl(true);
            }
            else {
                page.getReference().setUsePrettyUrl(true);
            }

            page.getReference().setTitle(a.text());

            page.setType("wiki");
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
        summary = new StringResource(context, wikiBaseDir + "summary.md", safe);
        summary.getReference().setTitle("Summary");

        WikiPage page = new WikiPage(summary);
        page.getReference().setUsePrettyUrl(true);

        page.setType("wiki");

        wiki.add(page);
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> menuItems = new ArrayList<>();

        OrchidIndex wikiIndex = context.getIndex().get("wiki");

        Map<String, OrchidIndex> wikiSections = wikiIndex.getChildren();

        for (Map.Entry<String, OrchidIndex> section : wikiSections.entrySet()) {
            List<OrchidPage> sectionPages = section.getValue().getAllPages();

            sectionPages.sort((OrchidPage o1, OrchidPage o2) -> {
                if (o1 instanceof WikiPage && o2 instanceof WikiPage) {
                    return ((WikiPage) o1).getOrder() - ((WikiPage) o2).getOrder();
                }
                else {
                    return 0;
                }
            });

            if(sectionPages.size() == 1) {
                menuItems.add(new OrchidMenuItem(context, sectionPages.get(0)));
            }
            else {
                menuItems.add(new OrchidMenuItem(context, section.getKey(), sectionPages));
            }

        }

        menuItems.sort((OrchidMenuItem o1, OrchidMenuItem o2) -> {
            OrchidPage p1 = (o1.isHasChildren()) ? o1.getChildren().get(0).getPage() : o1.getPage();
            OrchidPage p2 = (o2.isHasChildren()) ? o2.getChildren().get(0).getPage() : o2.getPage();

            if ((p1 != null && p1 instanceof WikiPage) && (p2 != null && p2 instanceof WikiPage)) {
                return ((WikiPage) p1).getOrder() - ((WikiPage) p2).getOrder();
            }
            else {
                return 0;
            }
        });


        return menuItems;
    }
}
