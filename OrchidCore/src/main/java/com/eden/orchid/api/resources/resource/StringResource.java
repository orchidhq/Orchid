package com.eden.orchid.api.resources.resource;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.json.JSONObject;

import java.util.Map;

/**
 * A Resource type that provides a plain String as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply the `page.content` variable to the template renderer. When used with renderRaw(), the raw
 * plain String content will be written directly instead.
 */
public final class StringResource extends OrchidResource {

    public StringResource(String content, OrchidReference reference) {
        super(reference);
        if(content != null) {
            EdenPair<String, Map<String, Object>> parsedContent = reference.getContext().getEmbeddedData(reference.getExtension(), content);
            this.rawContent = content;
            if(parsedContent != null) {
                this.content = parsedContent.first;
                this.embeddedData = new JSONElement(new JSONObject(parsedContent.second));
            }
            else {
                this.content = content;
                this.embeddedData = null;
            }
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }

    public StringResource(String content, OrchidReference reference, Map<String, Object> data) {
        this(content, reference, new JSONElement(new JSONObject(data)));
    }

    public StringResource(String content, OrchidReference reference, JSONElement data) {
        super(reference);
        if(content != null) {
            EdenPair<String, Map<String, Object>> parsedContent = reference.getContext().getEmbeddedData(reference.getExtension(), content);
            this.rawContent = content;
            this.content = parsedContent.first;
            this.embeddedData = data;
        }
        else {
            this.rawContent = "";
            this.content = "";
            this.embeddedData = null;
        }
    }

    public StringResource(OrchidContext context, String name, String content) {
        this(content, new OrchidReference(context, name));
    }

    public StringResource(OrchidContext context, String name, String content, Map<String, Object> data) {
        this(content, new OrchidReference(context, name));
        this.embeddedData = new JSONElement(new JSONObject(data));
    }
}
