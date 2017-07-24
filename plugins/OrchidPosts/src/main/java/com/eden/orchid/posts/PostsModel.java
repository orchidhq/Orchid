package com.eden.orchid.posts;

import com.eden.common.util.EdenPair;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import com.eden.orchid.posts.pages.PostTagArchivePage;
import lombok.Data;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Singleton
@Data
public class PostsModel {

    private Map<String, EdenPair<List<PostPage>, List<PostArchivePage>>> categories;
    private Map<String, EdenPair<List<PostPage>, List<PostTagArchivePage>>> tags;

    @Inject
    public PostsModel() {
        initialize();
    }

    void initialize() {
        this.categories = new LinkedHashMap<>();
        this.tags = new LinkedHashMap<>();
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
}
