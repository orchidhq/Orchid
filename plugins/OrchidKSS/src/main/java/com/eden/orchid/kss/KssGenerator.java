package com.eden.orchid.kss;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.kss.parser.KssParser;
import com.eden.orchid.kss.parser.StyleguideSection;
import com.eden.orchid.utilities.OrchidUtils;
import com.google.inject.Singleton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class KssGenerator extends OrchidGenerator {

    public static Map<String, List<KssPage>> sections;

    @Option("baseDir")
    @StringDefault("assets/css")
    public String styleguideBaseDir;

    @Option("sections")
    public String[] sectionNames;

    @Option
    public String stylesheet;

    @Inject
    public KssGenerator(OrchidContext context) {
        super(context, "styleguide", 700);
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        sections = new LinkedHashMap<>();

        if (EdenUtils.isEmpty(sectionNames)) {
            sections.put(null, getStyleguidePages(null));
        }
        else {
            for (String section : sectionNames) {
                sections.put(section, getStyleguidePages(section));
            }
        }

        List<KssPage> allPages = new ArrayList<>();
        for (String key : sections.keySet()) {
            allPages.addAll(sections.get(key));
        }

        return allPages;
    }

    private List<KssPage> getStyleguidePages(String section) {
        String sectionBaseDir = (!EdenUtils.isEmpty(section)) ?
                OrchidUtils.normalizePath(styleguideBaseDir) + "/" + OrchidUtils.normalizePath(section) + "/" :
                OrchidUtils.normalizePath(styleguideBaseDir) + "/";

        List<OrchidResource> resources = context.getLocalResourceEntries(sectionBaseDir, null, true);
        List<KssPage> pages = new ArrayList<>();

        KssParser parser = new KssParser(resources);
        for (Map.Entry<String, StyleguideSection> styleguideSection : parser.getStyleguideSections().entrySet()) {
            KssPage page = new KssPage(this.context, styleguideSection.getValue(), section, styleguideSection.getKey());

            if (!EdenUtils.isEmpty(stylesheet)) {
                page.setStylesheet(stylesheet);
            }

            pages.add(page);
        }

        try {
            for (KssPage page : pages) {
                for (KssPage otherPage : pages) {
                    if (page.getStyleguideSection().getDepth() > 1 && page.getStyleguideSection() != null) {
                        // find parent of [page]
                        if (otherPage.getStyleguideSection() == page.getStyleguideSection().getParent()) {
                            page.setParent(otherPage);
                        }
                    }

                    // also find children of [page]
                    if (otherPage.getStyleguideSection() != null && otherPage.getStyleguideSection().getParent() == page.getStyleguideSection()) {
                        page.getChildren().add(otherPage);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        return pages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        pages.forEach(context::renderTemplate);
    }
}