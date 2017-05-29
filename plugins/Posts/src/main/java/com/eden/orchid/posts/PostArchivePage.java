package com.eden.orchid.posts;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
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
