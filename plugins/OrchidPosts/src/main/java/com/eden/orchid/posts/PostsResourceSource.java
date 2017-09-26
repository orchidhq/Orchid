package com.eden.orchid.posts;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PostsResourceSource extends PluginResourceSource {

    @Inject
    public PostsResourceSource(OrchidContext context) {
        super(context, 20);
    }
}
