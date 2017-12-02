package com.eden.orchid.languages.highlighter;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;

public class HighlightResourceSource extends PluginResourceSource {

    @Inject
    public HighlightResourceSource(OrchidContext context) {
        super(context, 750);
    }

}