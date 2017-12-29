package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class DayPathType extends PermalinkPathType {

    @Inject
    public DayPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("day");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostPage) {
            return "" + ((PostPage) page).getDay();
        }

        return null;
    }

}
