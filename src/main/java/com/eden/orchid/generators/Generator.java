package com.eden.orchid.generators;

import com.eden.orchid.utilities.JSONElement;

public interface Generator {
    JSONElement startIndexing();
    void startGeneration();
    int priority();
    String getName();
}
