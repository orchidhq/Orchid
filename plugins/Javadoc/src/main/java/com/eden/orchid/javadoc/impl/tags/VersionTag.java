package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class VersionTag extends JavadocBlockTagHandler {

    @Inject
    public VersionTag() {
        this.priority = 20;
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
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
