package com.eden.orchid.javadoc.impl.generators;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class JavadocClassesGenerator extends OrchidGenerator {

    @Inject
    public JavadocClassesGenerator(OrchidContext context) {
        super(context);
        this.priority = 800;
    }

    @Override
    public String getName() {
        return "javadocClasses";
    }

    @Override
    public String getDescription() {
        return "Creates a page for each Class in your project, displaying the expected Javadoc information of methods, " +
                "fields, etc. but in your site's theme.";
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }
}