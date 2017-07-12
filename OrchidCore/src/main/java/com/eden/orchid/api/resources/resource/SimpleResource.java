package com.eden.orchid.api.resources.resource;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidReference;

/**
 * A Resource type that is little more than a Reference. it has no inherent content, and is intended to be instead just
 * "point" somewhere, such as with an external URL.
 */
public final class SimpleResource extends OrchidResource {

    public SimpleResource(OrchidReference reference) {
        super(reference);
    }

    public SimpleResource(OrchidContext context, String fullFileName) {
        this(new OrchidReference(context, fullFileName));
    }
}
