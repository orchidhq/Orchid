package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MockGenerator extends OrchidGenerator {

    List<? extends OrchidPage> mockPages;
    List<? extends OrchidPage> generatedPages;

    public MockGenerator(OrchidContext context, String key, int priority, List<? extends OrchidPage> mockPages) {
        super(context, key, priority);
        this.mockPages = mockPages;
    }

    @Override
    public List<OrchidPage> startIndexing() {
        return (List<OrchidPage>) mockPages;
    }

    @Override
    public void startGeneration(Stream<? extends OrchidPage> pages) {
        generatedPages = pages.collect(Collectors.toList());
    }
}
