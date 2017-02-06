package com.eden.orchid.docParser;

import com.eden.common.json.JSONElement;
import com.sun.javadoc.Tag;

/**
 * A BlockTagHandler processes Tags within Javadoc comments.
 */
public interface BlockTagHandler {

    JSONElement processTags(Tag[] tags);

    String getName();

    String getDescription();

    int priority();
}
