package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface OptionsHolder extends Descriptive {

    default void extractOptions(OrchidContext context, Map<String, Object> options) {
        OptionsExtractor extractor = context.resolve(OptionsExtractor.class);
        extractor.extractOptions(this, options);
        onPostExtraction();
    }

    default void extractOptions(OrchidContext context, JSONObject options) {
        this.extractOptions(context, options.toMap());
    }

    default void onPostExtraction() {

    }

    default OptionHolderDescription describeOptions(OrchidContext context) {
        OptionsExtractor extractor = context.resolve(OptionsExtractor.class);
        return extractor.describeAllOptions(this.getClass());
    }

    default List<String> getOptionNames(OrchidContext context) {
        OptionsExtractor extractor = context.resolve(OptionsExtractor.class);
        return extractor.getOptionNames(this.getClass());
    }

}
