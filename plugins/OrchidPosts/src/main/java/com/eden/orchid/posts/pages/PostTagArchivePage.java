package com.eden.orchid.posts.pages;

import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PostTagArchivePage extends PostArchivePage {

    public PostTagArchivePage(OrchidResource resource) {
        super(resource, "postTagArchive");
        this.setTemplates(new String[]{"postTagArchive", "postArchive"});
    }

    public String getTag() {
        return category;
    }

    public void setTag(String tag) {
        this.category = tag;
    }
}
