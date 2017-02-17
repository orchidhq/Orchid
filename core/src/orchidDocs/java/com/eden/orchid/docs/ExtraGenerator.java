package com.eden.orchid.docs;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.generators.OrchidGenerator;

public class ExtraGenerator extends OrchidGenerator {
    @Override
    public JSONElement startIndexing() {
        return null;
    }

    @Override
    public void startGeneration() {

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
