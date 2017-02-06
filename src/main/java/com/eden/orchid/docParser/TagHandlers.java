package com.eden.orchid.docParser;

import com.eden.orchid.utilities.AutoRegister;
import com.eden.orchid.utilities.RegistrationProvider;
import com.sun.javadoc.Tag;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

@AutoRegister
public class TagHandlers implements RegistrationProvider {

    public static Map<Integer, BlockTagHandler> blockTagHandlers = new TreeMap<>(Collections.reverseOrder());
    public static Map<Integer, InlineTagHandler> inlineTagHandlers = new TreeMap<>(Collections.reverseOrder());

    @Override
    public void register(Object object) {
        if (object instanceof BlockTagHandler) {
            BlockTagHandler blockTagHandler = (BlockTagHandler) object;
            int priority = blockTagHandler.priority();
            while (blockTagHandlers.containsKey(priority)) {
                priority--;
            }

            blockTagHandlers.put(priority, blockTagHandler);
        }

        if (object instanceof InlineTagHandler) {
            InlineTagHandler inlineTagHandler = (InlineTagHandler) object;
            int priority = inlineTagHandler.priority();
            while (inlineTagHandlers.containsKey(priority)) {
                priority--;
            }

            inlineTagHandlers.put(priority, inlineTagHandler);
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

