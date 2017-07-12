package com.eden.orchid.javadoc.tags.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ExceptionTag extends JavadocBlockTagHandler {

    @Inject
    public ExceptionTag() {
        this.priority = 70;
    }

    @Override
    public String getName() {
        return "exception";
    }

    @Override
    public String getDescription() {
        return "Annotate the Exception types that could be thrown by this method with reasons for the exception. Use" +
                "for Exceptions that are not declared within the 'throws' clause.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
