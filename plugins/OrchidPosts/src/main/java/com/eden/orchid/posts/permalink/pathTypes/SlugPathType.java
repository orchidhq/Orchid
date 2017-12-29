package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.PostsGenerator;
import com.eden.orchid.posts.PostsUtils;
import com.eden.orchid.posts.pages.PostArchivePage;
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
    public boolean acceptsKey(OrchidPage post, String key) {
        return key.equals("slug");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostPage) {
            PostPage post = (PostPage) page;
            String baseCategoryPath;

            if(EdenUtils.isEmpty(post.getCategory())) {
                baseCategoryPath = "posts";
            }
            else {
                baseCategoryPath = "posts/" + post.getCategory();
            }

            String formattedFilename = PostsUtils.getPostFilename(post.getResource(), baseCategoryPath);

            Matcher matcher = PostsGenerator.pageTitleRegex.matcher(formattedFilename);

            if (matcher.matches()) {
                return matcher.group(4);
            }
        }
        else if(page instanceof PostArchivePage) {
            return ((PostArchivePage) page).getCategory();
        }

        return null;
    }

}
