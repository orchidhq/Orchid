package com.eden.orchid.posts.pages;

import com.eden.orchid.api.options.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostPage extends OrchidPage {

    private int year;
    private int month;
    private int day;

    private String category;
    @Option protected String[] tags;
    @Option protected String featuredImage;

    public PostPage(OrchidResource resource) {
        super(resource, "post");
    }
}
