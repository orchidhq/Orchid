package com.eden.orchid.api.docParser;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.registration.Contextual;
import com.eden.orchid.api.registration.Prioritized;
import com.sun.javadoc.Tag;

/**
 * A OrchidBlockTagHandler processes Tags within Javadoc comments.
 */
public interface OrchidInlineTagHandler extends Prioritized, Contextual {

    JSONElement processTag(Tag tag);

    String getName();

    String getDescription();

}
