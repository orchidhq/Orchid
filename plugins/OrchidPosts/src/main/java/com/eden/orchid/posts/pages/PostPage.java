package com.eden.orchid.posts.pages;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.ApplyBaseUrl;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.Author;
import com.eden.orchid.posts.PostsExcerptStrategy;
import com.eden.orchid.posts.PostsModel;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter @Setter
public class PostPage extends OrchidPage {

    private PostsModel postsModel;

    private int year;
    private int month;
    private int day;

    private String category;

    @Option protected Author author;

    @Option protected String[] tags;

    @Option
    @ApplyBaseUrl
    protected String featuredImage;

    @Option protected String tagline;

    @Option protected String permalink;

    public PostPage(OrchidResource resource, PostsModel postsModel) {
        super(resource, "post");
        this.postsModel = postsModel;
    }

    public String publishedDate() {
        return publishedDate("MMMMM d, yyyy");
    }

    public String publishedDate(String format) {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DAY_OF_MONTH, day);

        return new SimpleDateFormat(format).format(date.getTime());
    }

    public String getExcerpt() {
        PostsExcerptStrategy strategy = context.getInjector().getInstance(PostsExcerptStrategy.class);
        return strategy.getExcerpt(this);
    }

    public PostArchivePage getCategoryPage() {
        return postsModel.getCategoryLandingPage(this.category);
    }

    public PostTagArchivePage getTagPage(String tag) {
        return postsModel.getTagLandingPage(tag);
    }

    public List<PostTagArchivePage> getTagPages() {
        List<PostTagArchivePage> tagArchivePages = new ArrayList<>();

        for (String tag : tags) {
            PostTagArchivePage tagArchivePage = getTagPage(tag);
            if (tagArchivePage != null) {
                tagArchivePages.add(getTagPage(tag));
            }
        }

        return tagArchivePages;
    }

    @Override
    public List<String> getTemplates() {
        List<String> templates = super.getTemplates();
        if(!EdenUtils.isEmpty(category)) {
            templates.add(0, category);
            templates.add(0, "post-" + category);
        }

        return templates;
    }
}
