package com.eden.orchid.generators;

import com.eden.common.json.JSONElement;

public interface Generator {
    JSONElement startIndexing();

    void startGeneration();

    int priority();

    String getName();
}
