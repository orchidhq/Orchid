package com.eden.orchid.presentations;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.options.annotations.StringDefault;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentationsGenerator extends OrchidGenerator {

    private final PresentationsModel model;

    @Option("baseDir")
    @StringDefault("presentations")
    public String presentationsBaseDir;

    @Option("presentations")
    public String[] sectionNames;

    @Inject
    public PresentationsGenerator(OrchidContext context, PresentationsModel model) {
        super(context, "presentations", 25);
        this.model = model;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        Map<String, Presentation> presentations = new HashMap<>();

        if (!EdenUtils.isEmpty(sectionNames)) {
            for (String section : sectionNames) {
                Presentation presentation = getPresentation(section);
                presentations.put(section, presentation);
            }
        }

        model.initialize(presentations);

        return null;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }

    private Presentation getPresentation(String section) {
        List<Slide> slides = new ArrayList<>();

        List<OrchidResource> resourcesList = context.getLocalResourceEntries(OrchidUtils.normalizePath(presentationsBaseDir) + "/" + section, null, false);
        for (OrchidResource entry : resourcesList) {
            slides.add(new Slide(entry));
        }

        return new Presentation(section, slides);
    }
}
