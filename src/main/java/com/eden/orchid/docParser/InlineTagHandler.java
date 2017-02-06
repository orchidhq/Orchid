package com.eden.orchid.docParser;

import com.eden.common.json.JSONElement;
import com.sun.javadoc.Tag;

/**
 * A BlockTagHandler processes Tags within Javadoc comments.
 */
public interface InlineTagHandler {

    JSONElement processTag(Tag tag);

    String getName();

    String getDescription();

    int priority();
}
