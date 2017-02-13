package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class SinceTag extends OrchidBlockTagHandler {

    @Inject
    public SinceTag() {
        this.priority = 20;
    }

    @Override
    public String getName() {
        return "since";
    }

    @Override
    public String getDescription() {
        return "The earliest version of the library since this member has existed.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
