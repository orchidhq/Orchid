package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;

public interface OptionsHolder {
    default void extractOptions(OrchidContext context, JSONObject options) {
        OptionsExtractor extractor = context.getInjector().getInstance(OptionsExtractor.class);
        extractor.extractOptions(this, options);
    }
}
