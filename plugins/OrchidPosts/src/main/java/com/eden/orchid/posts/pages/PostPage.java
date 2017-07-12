package com.eden.orchid.posts;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import lombok.Data;

import javax.inject.Inject;
import java.util.List;

@Data
public class PostPage extends OrchidPage {

    private PostsExcerptStrategy excerptStrategy;

    private int year;
    private int month;
    private int day;

    private String category;
    private List<String> tags;

    public PostPage(OrchidResource resource) {
        super(resource);

        this.setType("post");

        context.getInjector().injectMembers(this);
    }

    @Inject
    public void setExcerptStrategy(PostsExcerptStrategy excerptStrategy) {
        this.excerptStrategy = excerptStrategy;
    }

    public String getExcerpt() {
        return excerptStrategy.getExcerpt(this);
    }
}
