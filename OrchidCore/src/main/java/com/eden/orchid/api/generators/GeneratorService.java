package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.pages.OrchidPage;

import java.util.stream.Stream;

public interface GeneratorService extends OrchidService {

    default void startIndexing() {
        getService(GeneratorService.class).startIndexing();
    }

    default void startGeneration() {
        getService(GeneratorService.class).startGeneration();
    }

    default Stream<OrchidGenerator> getFilteredGenerators(boolean parallel) {
        return getService(GeneratorService.class).getFilteredGenerators(parallel);
    }

    default void onPageGenerated(OrchidPage page, long millis) {
        getService(GeneratorService.class).onPageGenerated(page, millis);
    }

}
