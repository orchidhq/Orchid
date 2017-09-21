package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;

import java.util.List;

public interface OptionsHolder {
    default void extractOptions(OrchidContext context, JSONObject options) {
        OptionsExtractor extractor = context.getInjector().getInstance(OptionsExtractor.class);
        extractor.extractOptions(this, options);
    }

    default List<OptionsDescription> describeOptions(OrchidContext context) {
        OptionsExtractor extractor = context.getInjector().getInstance(OptionsExtractor.class);
        return extractor.describeOptions(this);
    }
}
