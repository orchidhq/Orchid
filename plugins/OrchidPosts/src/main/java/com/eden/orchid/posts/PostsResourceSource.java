package com.eden.orchid.posts;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostsResourceSource extends DefaultResourceSource {

    @Inject
    public PostsResourceSource(OrchidContext context) {
        super(context);
        setPriority(20);
    }
}
