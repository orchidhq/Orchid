package com.eden.orchid.javadoc.tags.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ReturnTag extends JavadocBlockTagHandler {

    @Inject
    public ReturnTag() {
        super(60);
    }

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public String getDescription() {
        return "Describe the values that might be returned from this method.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
