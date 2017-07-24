package com.eden.orchid.posts.pages;

import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostPage extends OrchidPage {

    private int year;
    private int month;
    private int day;

    private String category;
    @Option protected String[] tags;

    public PostPage(OrchidResource resource) {
        super(resource, "post");
    }
}
