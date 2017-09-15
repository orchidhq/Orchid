package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidService;

public interface GeneratorService extends OrchidService {

    default void startIndexing() {
        getService(GeneratorService.class).startIndexing();
    }

    default void startGeneration() {
        getService(GeneratorService.class).startGeneration();
    }

}
