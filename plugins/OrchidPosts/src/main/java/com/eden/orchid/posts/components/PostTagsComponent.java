package com.eden.orchid.posts.components;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.OrchidResources;
import com.eden.orchid.api.theme.components.OrchidComponent;

import javax.inject.Inject;

public class PostTagsComponent extends OrchidComponent {

    @Inject
    public PostTagsComponent(OrchidContext context, OrchidResources resources) {
        super(25, "postTags", context, resources);
    }
}
