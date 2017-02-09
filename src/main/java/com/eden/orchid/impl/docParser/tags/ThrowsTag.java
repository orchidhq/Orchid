package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

public class ThrowsTag implements OrchidBlockTagHandler {

    @Override
    public int priority() {
        return 80;
    }

    @Override
    public String getName() {
        return "throws";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
