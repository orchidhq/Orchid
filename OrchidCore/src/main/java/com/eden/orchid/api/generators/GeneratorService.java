package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.theme.pages.OrchidPage;

public interface GeneratorService extends OrchidService {

    default void startIndexing() {
        getService(GeneratorService.class).startIndexing();
    }

    default void startGeneration() {
        getService(GeneratorService.class).startGeneration();
    }

    default void onPageGenerated(OrchidPage page, long millis) {
        getService(GeneratorService.class).onPageGenerated(page, millis);
    }

}
