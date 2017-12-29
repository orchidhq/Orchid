package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class MonthPathType extends PermalinkPathType {

    @Inject
    public MonthPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("month");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostPage) {
            return "" + ((PostPage) page).getMonth();
        }

        return null;
    }

}
