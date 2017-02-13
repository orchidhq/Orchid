package com.eden.orchid.wiki;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.Orchid;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;
import com.eden.orchid.api.resources.OrchidResource;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.impl.resources.StringResource;
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

    private List<OrchidPage> wiki;
    private JSONObject siteWiki;

    private String wikiBaseDir = "wiki/";

    private List<JSONObject> terms;

    private OrchidResources resources;
    private WikiPathOption option;

    @Inject
    public WikiGenerator(OrchidResources resources, WikiPathOption option) {
        this.resources = resources;
        this.option = option;

        this.priority = 700;
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
    public JSONElement startIndexing() {

        if(!EdenUtils.isEmpty(option.getPath())) {
            wikiBaseDir = option.getPath();
        }

        wiki = new ArrayList<>();
        siteWiki = new JSONObject();
        terms = new ArrayList<>();

        setupSummary();
        setupGlossary();

        if(siteWiki.has(wikiBaseDir.replace("/", ""))) {
            return new JSONElement(siteWiki.getJSONObject(wikiBaseDir.replace("/", "")));
        }
        else {
            return null;
        }
    }

    @Override
    public void startGeneration() {
        for (OrchidPage page : wiki) {
            if(!page.getReference().getFullPath().equalsIgnoreCase(wikiBaseDir + "glossary")) {
                findGlossaryTerms(page).renderTemplate("templates/pages/page.twig");
            }
            else {
                page.renderTemplate("templates/pages/page.twig");
            }
        }
    }

    private void setupIndexPage(JSONObject siteWiki, OrchidResource entry) {
        OrchidPage page = new OrchidPage(entry);
        page.getReference().setUsePrettyUrl(true);

        wiki.add(page);

        JSONObject index = new JSONObject();
        index.put("name", page.getReference().getTitle());
        index.put("url", page.getReference().toString());

        OrchidUtils.buildTaxonomy(entry, siteWiki, index);
    }

    private void setupGlossary() {
        OrchidResource glossary = resources.getResourceDirEntry(wikiBaseDir + "GLOSSARY.md");

        if(glossary == null) {
            return;
        }

        String content = Orchid.getContext().getTheme().compile(glossary.getReference().getExtension(), glossary.getContent());
        Document doc = Jsoup.parse(content);

        for (Element h2 : doc.select("h2")) {
            String id = h2.text().replaceAll("\\s+", "_").toLowerCase();
            String path = wikiBaseDir + "glossary/#" + id;
            String url = OrchidUtils.applyBaseUrl(path);

            Element link = new Element("a");
            link.attr("href", url);
            link.text(h2.text());

            h2.attr("id", id);
            h2.empty();
            h2.appendChild(link);

            JSONObject index = new JSONObject();
            index.put("name", h2.text());
            index.put("title", h2.text());
            index.put("url", url);

//            AssetsGenerator.buildTaxonomy(path, siteWiki, index);
            terms.add(index);
        }

        String safe = doc.toString();
        glossary = new StringResource(wikiBaseDir + "glossary.md", safe);
        glossary.getReference().setTitle("Glossary");

        setupIndexPage(siteWiki, glossary);
    }

    private void setupSummary() {
        OrchidResource summary = resources.getResourceDirEntry(wikiBaseDir + "SUMMARY.md");

        if(summary == null) {
            return;
        }

        String content = Orchid.getContext().getTheme().compile(summary.getReference().getExtension(), summary.getContent());
        Document doc = Jsoup.parse(content);

        Elements links = doc.select("a[href]");

        OrchidPage previous = null;

        for (Element a : links) {
            String file = wikiBaseDir + a.attr("href");
            String path = wikiBaseDir + FilenameUtils.removeExtension(a.attr("href"));

            OrchidResource resource = resources.getResourceDirEntry(file);

            if(resource == null) {
                Clog.w("Could not find wiki resource page at '#{$1}'", new Object[]{ file });
                resource = new StringResource(path + File.separator + "index.md", a.text());
            }

            OrchidPage page = new OrchidPage(resource);

            if(!EdenUtils.isEmpty(FilenameUtils.getExtension(a.attr("href")))) {
                page.getReference().setUsePrettyUrl(true);
            }
            else {
                page.getReference().setUsePrettyUrl(true);
            }

            page.getReference().setTitle(a.text());

            wiki.add(page);

            JSONObject index = new JSONObject();
            index.put("name", a.text());
            index.put("title", a.text());
            index.put("url", page.getReference().toString());

            OrchidUtils.buildTaxonomy(path, siteWiki, index);

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
        summary = new StringResource(wikiBaseDir + "summary.md", safe);
        summary.getReference().setTitle("Summary");

        setupIndexPage(siteWiki, summary);
    }

    private OrchidPage findGlossaryTerms(OrchidPage page) {
        String content = page.getResource().getContent();

        for(JSONObject term : terms) {
            if(content.contains(term.getString("name"))) {
                content = content.replaceAll(term.getString("name"), Clog.format("<a href=\"#{$1}\">#{$2}</a>", new Object[]{ term.getString("url"), term.getString("name") }));
            }
        }

        page.setResource(new StringResource(content, page.getResource().getReference()));
        return page;
    }
}
