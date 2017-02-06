package com.eden.orchid.impl.docParser.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.docParser.BlockTagHandler;
import com.eden.orchid.docParser.InlineTagHandler;
import com.eden.orchid.utilities.AutoRegister;
import com.sun.javadoc.Tag;
import org.json.JSONArray;

@AutoRegister
public class SeeTag implements InlineTagHandler, BlockTagHandler {
    @Override
    public int priority() {
        return 40;
    }

    @Override
    public String getName() {
        return "see";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        JSONArray array = new JSONArray();

        for(Tag tag : tags) {
            array.put(tag.text());
        }

        return new JSONElement(array);
    }

    @Override
    public JSONElement processTag(Tag tag) {
        return new JSONElement(tag.text());
    }
}
