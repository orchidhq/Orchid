package com.eden.orchid.api.resources.resource;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;

/**
 * A Resource type that provides a plain String as content to a template. When used with renderTemplate() or renderString(),
 * this resource will supply the `page.content` variable to the template renderer. When used with renderRaw(), the raw
 * plain String content will be written directly instead.
 */
public final class SimpleResource extends OrchidResource {

    public SimpleResource(OrchidReference reference) {
        super(reference);
    }

    public SimpleResource(OrchidContext context, String fullFileName) {
        this(new OrchidReference(context, fullFileName));
    }
}
