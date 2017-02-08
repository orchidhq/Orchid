package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.registration.AutoRegister;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

@AutoRegister
public class SinceTag implements OrchidBlockTagHandler {
    @Override
    public int priority() {
        return 20;
    }

    @Override
    public String getName() {
        return "since";
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
