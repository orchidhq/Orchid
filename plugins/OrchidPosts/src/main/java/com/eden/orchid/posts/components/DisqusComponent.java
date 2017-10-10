package com.eden.orchid.posts.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class DisqusComponent extends OrchidComponent {

    @Option
    public String shortname;

    @Option
    public String identifier;

    @Inject
    public DisqusComponent(OrchidContext context) {
        super(context, "disqus", 100);
    }

}
