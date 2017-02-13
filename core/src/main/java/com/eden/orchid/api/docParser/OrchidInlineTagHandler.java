package com.eden.orchid.api.docParser;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Prioritized;
import com.sun.javadoc.Tag;

/**
 * A OrchidBlockTagHandler processes Tags within Javadoc comments.
 */
public abstract class OrchidInlineTagHandler extends Prioritized {

    public abstract JSONElement processTag(Tag tag);

    public abstract String getName();

    public abstract String getDescription();

}
