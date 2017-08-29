package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class DayPathType extends PermalinkPathType {

    @Inject
    public DayPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("day");
    }

    @Override
    public String format(PostPage post, String key) {
        return "" + post.getDay();
    }

}
