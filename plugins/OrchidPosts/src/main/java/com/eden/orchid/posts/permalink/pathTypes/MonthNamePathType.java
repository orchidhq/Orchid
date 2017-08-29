package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;
import java.time.Month;

public class MonthNamePathType extends PermalinkPathType {

    @Inject
    public MonthNamePathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("monthName");
    }

    @Override
    public String format(PostPage post, String key) {
        return Month.of(post.getMonth()).toString();
    }

}
