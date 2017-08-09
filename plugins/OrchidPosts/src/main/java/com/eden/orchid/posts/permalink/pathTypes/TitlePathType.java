package com.eden.orchid.posts.permalink.pathTypes;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class TitlePathType extends PermalinkPathType {

    @Inject
    public TitlePathType() {
        this.priority = 100;
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("title");
    }

    @Override
    public String format(PostPage post, String key) {
        Clog.v("title key: {} - {}", key, post.getTitle());
        return post.getTitle();
    }

}
