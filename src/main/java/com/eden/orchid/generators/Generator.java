package com.eden.orchid.generators;

import com.eden.orchid.JSONElement;
import com.sun.javadoc.RootDoc;

public interface Generator {
    JSONElement startDiscovery(RootDoc root, int iteration);
    int priority();
    String getName();
}
