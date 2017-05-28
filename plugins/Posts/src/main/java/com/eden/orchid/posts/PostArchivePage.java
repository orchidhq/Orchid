package com.eden.orchid.posts;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Data;

@Data
public class PostArchivePage extends OrchidPage {

    protected String category;
    protected int page;
    protected int pageSize;
    protected int totalPages;

    public PostArchivePage(OrchidResource resource) {
        super(resource);

        this.type = "postArchive";
    }
}
