package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.posts.PostsGenerator;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;
import java.util.regex.Matcher;

public class SlugPathType extends PermalinkPathType {

    @Inject
    public SlugPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(PostPage post, String key) {
        return key.equals("slug");
    }

    @Override
    public String format(PostPage post, String key) {
        Matcher matcher = PostsGenerator.pageTitleRegex.matcher(post.getReference().getFileName());

        if (matcher.matches()) {
            return matcher.group(4);
        }
        else {
            return null;
        }
    }

}
