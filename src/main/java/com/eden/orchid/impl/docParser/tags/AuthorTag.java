package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

public class AuthorTag implements OrchidBlockTagHandler {

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public String getName() {
        return "author";
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
