package com.eden.orchid.posts.permalink;

import com.eden.orchid.api.registration.Prioritized;
import com.eden.orchid.posts.pages.PostPage;

public abstract class PermalinkPathType extends Prioritized {

    public abstract boolean acceptsKey(PostPage post, String key);

    public abstract String format(PostPage post, String key);

}
