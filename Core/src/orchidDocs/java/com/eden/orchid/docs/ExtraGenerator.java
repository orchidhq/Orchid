package com.eden.orchid.docs;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.OrchidPage;

import java.util.List;

public class ExtraGenerator extends OrchidGenerator {
    @Override
    public List<OrchidPage> startIndexing() {
        return null;
    }

    @Override
    public void startGeneration(List<OrchidPage> pages) {

    }

    @Override
    public String getName() {
        return "extraGenerator";
    }

    @Override
    public String getDescription() {
        return "Just an extra generator, does nothing right now";
    }
}
