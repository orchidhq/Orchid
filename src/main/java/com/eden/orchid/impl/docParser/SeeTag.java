package com.eden.orchid.impl.docParser;

import com.eden.common.json.JSONElement;
import com.eden.orchid.docParser.TagHandler;
import com.eden.orchid.utilities.AutoRegister;
import com.sun.javadoc.Tag;
import org.json.JSONArray;

@AutoRegister
public class SeeTag implements TagHandler {
    @Override
    public int priority() {
        return 40;
    }

    @Override
    public String getName() {
        return "see";
    }

    @Override
    public JSONElement processTag(Tag[] tags) {
        JSONArray array = new JSONArray();

        for(Tag tag : tags) {
            array.put(tag.text());
        }

        return new JSONElement(array);
    }
}
