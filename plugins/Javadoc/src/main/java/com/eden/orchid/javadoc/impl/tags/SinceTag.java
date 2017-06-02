package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class SinceTag extends JavadocBlockTagHandler {

    @Inject
    public SinceTag() {
        this.priority = 20;
    }

    @Override
    public String getName() {
        return "since";
    }

    @Override
    public String getDescription() {
        return "The earliest version of the library since this member has existed.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
