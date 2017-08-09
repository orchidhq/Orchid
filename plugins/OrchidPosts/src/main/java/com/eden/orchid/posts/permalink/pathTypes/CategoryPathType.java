package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class CategoryPathType extends PermalinkPathType {

    @Inject
    public CategoryPathType() {
        this.priority = 100;
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("category");
    }

    @Override
    public String format(PostPage post, String key) {
        return post.getCategory();
    }

}
