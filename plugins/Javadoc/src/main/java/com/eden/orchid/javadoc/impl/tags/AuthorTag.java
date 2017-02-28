package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
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
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
