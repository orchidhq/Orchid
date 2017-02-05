package com.eden.orchid.docParser;

import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.javadoc.Tag;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@AutoRegister
public class TagHandlers implements RegistrationProvider {

    public static Map<Integer, TagHandler> tagHandlers = new TreeMap<>(Collections.reverseOrder());

    @Override
    public void register(Object object) {
        if (object instanceof TagHandler) {
            TagHandler tagHandler = (TagHandler) object;
            int priority = tagHandler.priority();
            while (tagHandlers.containsKey(priority)) {
                priority--;
            }

            tagHandlers.put(priority, tagHandler);
        }
    }

    public static String getText(Tag[] tags) {
        String text = "";
        for(Tag tag : tags) {
            text += tag.text();
        }
        return text;
    }
}

