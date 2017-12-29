package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;
import java.time.Month;

public class MonthNamePathType extends PermalinkPathType {

    @Inject
    public MonthNamePathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("monthName");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostPage) {
            return Month.of(((PostPage) page).getMonth()).toString();
        }

        return null;
    }

}
