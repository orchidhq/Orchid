package com.eden.orchid.posts.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
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

    @Override
    public List<String> getTemplates() {
        List<String> templates = super.getTemplates();
        if(!EdenUtils.isEmpty(category)) {
            templates.add(0, "postTagArchive-" + category);
        }

        return templates;
    }
}
