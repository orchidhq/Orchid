package com.eden.orchid.generators;

import com.eden.orchid.JSONElement;
import com.sun.javadoc.RootDoc;

public interface Generator {
    JSONElement startIndexing(RootDoc root);
    JSONElement startGeneration(RootDoc root);
    int priority();
    String getName();
}
