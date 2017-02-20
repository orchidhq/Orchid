package com.eden.orchid.api.render;

import com.eden.orchid.api.resources.OrchidReference;
import org.json.JSONObject;

public interface OrchidRenderer {
    boolean render(
            String template,
            String extension,
            boolean templateReference,
            JSONObject pageData,
            String alias,
            OrchidReference reference);
}
