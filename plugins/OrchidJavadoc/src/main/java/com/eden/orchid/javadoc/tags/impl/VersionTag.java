package com.eden.orchid.javadoc.tags.impl;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.OrchidJavadoc;
import com.eden.orchid.javadoc.tags.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class VersionTag extends JavadocBlockTagHandler {

    @Inject
    public VersionTag() {
        super(20);
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "For classes and interfaces only. Used to describe the version of this particular class, as a patch-version" +
                "of the library release version for every edit of this class.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidJavadoc.getText(tags));
    }
}
