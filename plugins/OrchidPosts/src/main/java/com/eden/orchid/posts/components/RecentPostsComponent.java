package com.eden.orchid.posts.components;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.annotations.Option;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.posts.PostsModel;
import com.eden.orchid.posts.pages.PostPage;

import javax.inject.Inject;
import java.util.List;

public class RecentPostsComponent extends OrchidComponent {

    public PostsModel postsModel;

    @Option public String category;
    @Option public int limit;

    @Inject
    public RecentPostsComponent(OrchidContext context, PostsModel postsModel) {
        super(context, "recentPosts", 25);
        this.postsModel = postsModel;
    }

    public List<PostPage> getRecentPosts() {
        if(!EdenUtils.isEmpty(category)) {
            return postsModel.getRecentPosts(category, limit);
        }
        else {
            return postsModel.getRecentPosts(null, limit);
        }
    }
}
