package com.eden.orchid.posts.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class PostTagsComponent extends OrchidComponent {

    @Inject
    public PostTagsComponent(OrchidContext context) {
        super(context, "postTags", 25);
    }
}
