package com.eden.orchid.docParser;

import com.eden.common.json.JSONElement;
import com.sun.javadoc.Tag;

public interface TagHandler {
    int priority();

    String getName();

    JSONElement processTag(Tag[] tags);
}
