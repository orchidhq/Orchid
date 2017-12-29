package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class ArchiveIndexPathType extends PermalinkPathType {

    @Inject
    public ArchiveIndexPathType() {
        super(100);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return key.equals("archiveIndex");
    }

    @Override
    public String format(OrchidPage page, String key) {
        if(page instanceof PostArchivePage) {

            int index = ((PostArchivePage) page).getIndex();

            return (index == 1) ? "" : "" + index;
        }

        return null;
    }

}
