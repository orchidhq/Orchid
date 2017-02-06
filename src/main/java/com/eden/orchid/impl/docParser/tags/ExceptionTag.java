package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.docParser.BlockTagHandler;
import com.eden.orchid.docParser.TagHandlers;
import com.eden.orchid.utilities.AutoRegister;
import com.sun.javadoc.Tag;

@AutoRegister
public class ExceptionTag implements BlockTagHandler {
    @Override
    public int priority() {
        return 70;
    }

    @Override
    public String getName() {
        return "exception";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        return new JSONElement(TagHandlers.getText(tags));
    }
}
