package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;

public class MockGenerator extends OrchidGenerator {

    List<? extends OrchidPage> mockPages;
    List<? extends OrchidPage> generatedPages;

    public MockGenerator(OrchidContext context, String key, int priority, List<? extends OrchidPage> mockPages) {
        super(context, key, priority);
        this.mockPages = mockPages;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<? extends OrchidPage> startIndexing() {
        return mockPages;
    }

    @Override
    public void startGeneration(List<? extends OrchidPage> pages) {
        generatedPages = pages;
    }
}
