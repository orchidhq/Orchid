package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class DataPropertyPathType extends PermalinkPathType {

    @Inject
    public DataPropertyPathType() {
        super(1);
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return post.getData().has(key);
    }

    @Override
    public String format(PostPage post, String key) {
        return post.getData().get(key).toString();
    }

}
