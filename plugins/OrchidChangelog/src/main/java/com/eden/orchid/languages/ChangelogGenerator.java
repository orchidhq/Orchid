package com.eden.orchid.languages;


import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class ChangelogGenerator extends OrchidGenerator {

    @Inject
    public ChangelogGenerator(OrchidContext context) {
        super(context);
        this.priority = 700;
    }

    @Override
    public String getName() {
        return "changelog";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {

    }
}