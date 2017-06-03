package com.eden.orchid.javadoc.api;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Prioritized;
import com.sun.javadoc.Tag;

/**
 * A JavadocBlockTagHandler processes Tags within Javadoc comments.
 */
public abstract class JavadocBlockTagHandler extends Prioritized {

    public abstract JSONElement processTags(Tag[] tags);

    public abstract String getName();

    public abstract String getDescription();

}
