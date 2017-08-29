package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class YearPathType extends PermalinkPathType {

    @Inject
    public YearPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("year");
    }

    @Override
    public String format(PostPage post, String key) {
        return "" + post.getYear();
    }

}
