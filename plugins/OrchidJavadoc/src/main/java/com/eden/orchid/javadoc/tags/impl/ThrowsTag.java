package com.eden.orchid.javadoc.tags.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ThrowsTag extends JavadocBlockTagHandler {

    @Inject
    public ThrowsTag() {
        this.priority = 80;
    }

    @Override
    public String getName() {
        return "throws";
    }

    @Override
    public String getDescription() {
        return "Describe the Exception types that have been declared with the 'throws' clause.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
