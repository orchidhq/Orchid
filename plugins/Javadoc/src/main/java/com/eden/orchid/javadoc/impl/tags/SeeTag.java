package com.eden.orchid.javadoc.impl.tags;

import com.eden.common.json.JSONElement;
import com.eden.orchid.javadoc.api.JavadocBlockTagHandler;
import com.sun.javadoc.Tag;
import org.json.JSONArray;

import javax.inject.Inject;

public class SeeTag extends JavadocBlockTagHandler {

    @Inject
    public SeeTag() {
        this.priority = 40;
    }

    @Override
    public String getName() {
        return "see";
    }

    @Override
    public String getDescription() {
        return "Create a link to another document indexed by Orchid. Not limited to Javadoc links.";
    }

    @Override
    public JSONElement processTags(Tag[] tags) {
        JSONArray array = new JSONArray();

        for(Tag tag : tags) {
            array.put(tag.text());
        }

        return new JSONElement(array);
    }
}
