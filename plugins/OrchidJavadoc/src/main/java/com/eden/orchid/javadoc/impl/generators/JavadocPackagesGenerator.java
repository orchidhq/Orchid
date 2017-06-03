package com.eden.orchid.javadoc.impl.generators;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class JavadocPackagesGenerator extends OrchidGenerator {

    @Inject
    public JavadocPackagesGenerator(OrchidContext context) {
        super(context);
        this.priority = 800;
    }

    @Override
    public String getName() {
        return "javadocPackages";
    }

    @Override
    public String getDescription() {
        return "Creates pages for each package in your project, displaying the classes contained within the package.";
    }

    @Override
    public List<OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }
}