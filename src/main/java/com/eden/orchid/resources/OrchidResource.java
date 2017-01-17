package com.eden.orchid.resources;

import com.eden.orchid.utilities.JSONElement;

public interface OrchidResource {

    String getContent();
    String getRawContent();
    JSONElement getEmbeddedData();
    JSONElement queryEmbeddedData(String pointer);

    String getFileName();
    String getFilePath();
    int getPriority();

}
