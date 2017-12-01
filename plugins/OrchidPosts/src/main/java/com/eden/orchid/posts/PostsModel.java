package com.eden.orchid.posts;

import com.eden.common.util.EdenPair;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.pages.PostTagArchivePage;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter @Setter
@Singleton
public class PostsModel {

    private String permalink;
    private String layout;
    private String excerptSeparator;
    private List<Author> authors;

    private Map<String, EdenPair<List<PostPage>, List<PostArchivePage>>> categories;
    private Map<String, EdenPair<List<PostPage>, List<PostTagArchivePage>>> tags;

    @Inject
    public PostsModel() {
        this.categories = new LinkedHashMap<>();
        this.tags = new LinkedHashMap<>();
    }

    void initialize(String permalink, String layout, String excerptSeparator, List<Author> authors) {
        this.categories = new LinkedHashMap<>();
        this.tags = new LinkedHashMap<>();

        this.permalink = permalink;
        this.layout = layout;
        this.excerptSeparator = excerptSeparator;
        this.authors = authors;
    }

    public Author getAuthorByName(String authorName) {
        if (!EdenUtils.isEmpty(authors)) {
            for (Author author : authors) {
                if (author.name.equalsIgnoreCase(authorName)) {
                    return author;
                }
            }
        }

        return null;
    }

    public Set<String> getCategoryNames() {
        return categories.keySet();
    }

    void tagPost(String tag, PostPage post) {
        tags.putIfAbsent(tag, new EdenPair<>(new ArrayList<>(), new ArrayList<>()));
        tags.get(tag).first.add(post);
    }

    public Set<String> getTagNames() {
        return tags.keySet();
    }

    public List<PostPage> getPostsTagged(String tag) {
        return tags.get(tag).first;
    }

    public List<PostPage> getRecentPosts(String category, int limit) {
        List<PostPage> chosenCategory = new ArrayList<>();

        if(category.equalsIgnoreCase(":any")) {
            chosenCategory = new ArrayList<>();

            for (EdenPair<List<PostPage>, List<PostArchivePage>> categoryPosts : categories.values()) {
                chosenCategory.addAll(categoryPosts.first);
            }
        }
        else {
            if (categories.containsKey(category)) {
                chosenCategory = categories.get(category).first;
            }
        }

        chosenCategory.sort(PostsModel.postPageComparator);


        List<PostPage> postPages = new ArrayList<>();

        if (limit >= chosenCategory.size()) {
            limit = chosenCategory.size();
        }

        for (int i = 0; i < limit; i++) {
            postPages.add(chosenCategory.get(i));
        }

        return postPages;
    }

    public PostArchivePage getCategoryLandingPage(String category) {
        if (categories.containsKey(category)) {
            return categories.get(category).second.get(0);
        }

        return null;
    }

    public PostTagArchivePage getTagLandingPage(String tag) {
        if (tags.containsKey(tag)) {
            return tags.get(tag).second.get(0);
        }

        return null;
    }

    public static Comparator<PostPage> postPageComparator = (o1, o2) -> {
        String[] criteria = new String[]{"year", "month", "day"};

        for (String item : criteria) {
            int result = 0;

            switch (item) {
                case "year":
                    result = o2.getYear() - o1.getYear();
                    break;
                case "month":
                    result = o2.getMonth() - o1.getMonth();
                    break;
                case "day":
                    result = o2.getDay() - o1.getDay();
                    break;
            }

            if (result != 0) {
                return result;
            }
        }

        return 0;
    };
}
