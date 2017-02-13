package com.eden.orchid.api.docParser;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Prioritized;
import com.sun.javadoc.Tag;

/**
 * A OrchidBlockTagHandler processes Tags within Javadoc comments.
 */
public abstract class OrchidBlockTagHandler extends Prioritized {

    public abstract JSONElement processTags(Tag[] tags);

    public abstract String getName();

    public abstract String getDescription();

}
