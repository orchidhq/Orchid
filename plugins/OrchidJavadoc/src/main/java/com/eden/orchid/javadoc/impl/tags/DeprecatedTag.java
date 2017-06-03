package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class DeprecatedTag extends JavadocBlockTagHandler {

    @Inject
    public DeprecatedTag() {
        this.priority = 90;
    }

    @Override
    public String getName() {
        return "deprecated";
    }

    @Override
    public String getDescription() {
        return "Mark this member as deprecated with a reason or what to use instead.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
