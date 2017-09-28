package com.eden.orchid.posts;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.regex.Pattern;

public class PostsExcerptStrategy {

    private final PostsModel postsModel;

    @Inject
    public PostsExcerptStrategy(PostsModel postsModel) {
        this.postsModel = postsModel;
    }

    public String getExcerpt(OrchidPage page) {
        String excerptSeparator = OrchidUtils.normalizePath(postsModel.getExcerptSeparator());
        String content = page.getContent();

        Pattern pattern = Pattern.compile(excerptSeparator, Pattern.DOTALL | Pattern.MULTILINE);

        if(pattern.matcher(content).find()) {
            return pattern.split(content)[0];
        }
        else {
            if(content.length() > 240) {
                return content.substring(0, 240) + "...";
            }
            else {
                return content;
            }
        }
    }
}
