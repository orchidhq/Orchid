package com.eden.orchid.posts.pages;

import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.PostsExcerptStrategy;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.inject.Inject;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class PostPage extends OrchidPage {

    private PostsExcerptStrategy excerptStrategy;

    private int year;
    private int month;
    private int day;

    private String category;
    private List<String> tags;

    public PostPage(OrchidResource resource) {
        super(resource, "post");
    }

    @Inject
    public void setExcerptStrategy(PostsExcerptStrategy excerptStrategy) {
        this.excerptStrategy = excerptStrategy;
    }

    public String getExcerpt() {
        return excerptStrategy.getExcerpt(this);
    }
}
