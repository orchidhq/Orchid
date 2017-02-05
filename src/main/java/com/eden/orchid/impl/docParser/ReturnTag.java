package com.eden.orchid.impl.docParser;

import com.eden.common.json.JSONElement;
import com.eden.orchid.docParser.TagHandler;
import com.eden.orchid.docParser.TagHandlers;
import com.eden.orchid.utilities.AutoRegister;
import com.sun.javadoc.Tag;

@AutoRegister
public class ReturnTag implements TagHandler {
    @Override
    public int priority() {
        return 60;
    }

    @Override
    public String getName() {
        return "return";
    }

    @Override
    public JSONElement processTag(Tag[] tags) {
        return new JSONElement(TagHandlers.getText(tags));
    }
}
