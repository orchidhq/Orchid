package com.eden.orchid.impl.docParser.tags;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.docParser.OrchidBlockTagHandler;
import com.eden.orchid.api.docParser.OrchidInlineTagHandler;

public class TagsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidInlineTagHandler.class, SeeTag.class);

        addToSet(OrchidBlockTagHandler.class, AuthorTag.class);
        addToSet(OrchidBlockTagHandler.class, DeprecatedTag.class);
        addToSet(OrchidBlockTagHandler.class, ExceptionTag.class);
        addToSet(OrchidBlockTagHandler.class, ReturnTag.class);
        addToSet(OrchidBlockTagHandler.class, SeeTag.class);
        addToSet(OrchidBlockTagHandler.class, SinceTag.class);
        addToSet(OrchidBlockTagHandler.class, ThrowsTag.class);
        addToSet(OrchidBlockTagHandler.class, VersionTag.class);
    }
}
