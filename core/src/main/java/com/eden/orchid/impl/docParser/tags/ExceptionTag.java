package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.utilities.OrchidUtils;
import com.sun.javadoc.Tag;

import javax.inject.Inject;

public class ExceptionTag extends OrchidBlockTagHandler {

    @Inject
    public ExceptionTag() {
        this.priority = 70;
    }

    @Override
    public String getName() {
        return "exception";
    }

    @Override
    public String getDescription() {
        return "Annotate the Exception types that could be thrown by this method with reasons for the exception. Use" +
                "for Exceptions that are not declared within the 'throws' clause.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(OrchidUtils.getText(tags));
    }
}
