package com.eden.orchid.javadoc.tags.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class AuthorTag extends JavadocBlockTagHandler {

    @Inject
    public AuthorTag() {
        this.priority = 10;
    }

    @Override
    public String getName() {
        return "author";
    }

    @Override
    public String getDescription() {
        return "The author of this annotated section of code.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
