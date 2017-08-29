package com.eden.orchid.posts.menu;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.posts.PostsModel;
import com.eden.orchid.posts.pages.PostArchivePage;
import com.eden.orchid.posts.pages.PostPage;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class LatestPostsMenuType implements OrchidMenuItemFactory {

    private OrchidContext context;
    private PostsModel postsModel;

    @Inject
    public LatestPostsMenuType(OrchidContext context, PostsModel postsModel) {
        this.context = context;
        this.postsModel = postsModel;
    }

    @Override
    public String getKey() {
        return "latestPosts";
    }

    @Override
    public List<OrchidMenuItem> getMenuItems(JSONObject menuItemJson) {
        List<OrchidMenuItem> items = new ArrayList<>();

        EdenPair<List<PostPage>, List<PostArchivePage>> category;
        String categoryName;
        int latestPostCount = (menuItemJson.has("count")) ? menuItemJson.getInt("count") : 10;

        if (context.query("options.posts.categories") != null && menuItemJson.has("category")) {
            category = postsModel.getCategories().get(menuItemJson.getString("category"));
            categoryName = menuItemJson.getString("category");
        }
        else {
            category = postsModel.getCategories().get(null);
            categoryName = "blog";
        }

        items.add(new OrchidMenuItem(
                context,
                "Latest from " + categoryName,
                new ArrayList<>(category.first.subList(0, Math.min(category.first.size(), latestPostCount)))));

        return items;
    }

}
