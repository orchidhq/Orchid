package com.eden.orchid.api.resources.resource;

import com.eden.orchid.utilities.OrchidUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * A Resource type that wraps another resource, optionally applying a transformation along the way.
 */
public class ResourceTransformation extends ResourceWrapper {

    private final List<Function<String, String>> contentTransformations;
    private final List<Function<InputStream, InputStream>> contentStreamTransformations;

    public ResourceTransformation(OrchidResource resource, Function<String, String>... contentTransformations) {
        this(resource, Arrays.asList(contentTransformations), null);
    }

    public ResourceTransformation(OrchidResource resource, List<Function<String, String>> contentTransformations, List<Function<InputStream, InputStream>> contentStreamTransformations) {
        super(resource);
        this.contentTransformations = (contentTransformations != null) ? contentTransformations : new ArrayList<>();
        this.contentStreamTransformations = (contentStreamTransformations != null) ? contentStreamTransformations : new ArrayList<>();
    }

    @Override
    public String getContent() {
        return OrchidUtils.transform(super.getContent(), contentTransformations);
    }

    @Override
    public InputStream getContentStream() {
        return OrchidUtils.transform(super.getContentStream(), contentStreamTransformations);
    }

}