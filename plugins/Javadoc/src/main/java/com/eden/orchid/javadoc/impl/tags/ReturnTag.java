package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ReturnTag extends JavadocBlockTagHandler {

    @Inject
    public ReturnTag() {
        this.priority = 60;
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
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
