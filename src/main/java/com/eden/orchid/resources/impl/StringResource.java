package com.eden.orchid.resources.impl;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;

public final class StringResource extends OrchidResource {
    public StringResource(String name, String content) {
        super(new OrchidReference(name));

        if(content != null) {
            EdenPair<String, JSONElement> parsedContent = Orchid.getTheme().getEmbeddedData(content);
            this.rawContent = content;
            this.content = parsedContent.first;
            this.embeddedData = parsedContent.second;
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }
}
