package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ThrowsTag extends OrchidBlockTagHandler {

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
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
