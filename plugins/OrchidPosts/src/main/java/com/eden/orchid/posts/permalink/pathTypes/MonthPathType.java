package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class MonthPathType extends PermalinkPathType {

    @Inject
    public MonthPathType() {
        this.priority = 100;
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("month");
    }

    @Override
    public String format(PostPage post, String key) {
        return "" + post.getMonth();
    }

}
