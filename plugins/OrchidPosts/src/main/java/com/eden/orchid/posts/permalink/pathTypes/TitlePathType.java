package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class TitlePathType extends PermalinkPathType {

    @Inject
    public TitlePathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("title");
    }

    @Override
    public String format(OrchidPage page, String key) {
        return page.getTitle();
    }

}
