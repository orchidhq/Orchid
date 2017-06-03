package com.eden.orchid.posts;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.utilities.OrchidUtils;

import javax.inject.Inject;
import java.util.regex.Pattern;

public class PostsExcerptStrategy {

    private OrchidContext context;

    @Inject
    public PostsExcerptStrategy(OrchidContext context) {
        this.context = context;
    }

    public String getExcerpt(OrchidPage page) {
        String excerptSeparator = null;

        if (page.getData().has("excerpt_separator")) {
            excerptSeparator = page.getData().getString("excerpt_separator");
        }
        else if (!EdenUtils.isEmpty(context.query("options.posts.excerpt_separator"))) {
            excerptSeparator = context.query("options.posts.excerpt_separator").toString();
        }
        else if (!EdenUtils.isEmpty(context.query("options.excerpt_separator"))) {
            excerptSeparator = context.query("options.excerpt_separator").toString();
        }

        excerptSeparator = OrchidUtils.normalizePath(excerptSeparator);

        String content = page.getContent();

        if(EdenUtils.isEmpty(excerptSeparator)) {
            excerptSeparator = "<!--more-->";
        }

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
