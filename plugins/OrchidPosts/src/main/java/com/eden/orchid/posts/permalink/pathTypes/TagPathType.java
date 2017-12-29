package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.pages.PostTagArchivePage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class TagPathType extends PermalinkPathType {

    @Inject
    public TagPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("tag");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostTagArchivePage) {
            return ((PostTagArchivePage) page).getTag();
        }

        return null;
    }

}
