package com.eden.orchid.impl.resources;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.Orchid;
import com.eden.orchid.resources.OrchidReference;
import com.eden.orchid.resources.OrchidResource;

public final class StringResource extends OrchidResource {

    public StringResource(String content, OrchidReference reference) {
        super(reference);
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

    public StringResource(String name, String content) {
        this(content, new OrchidReference(name));
    }
}
