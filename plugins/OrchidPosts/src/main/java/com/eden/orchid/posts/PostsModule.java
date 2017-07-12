package com.eden.orchid.posts;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.menus.OrchidMenuItemType;
import com.eden.orchid.posts.menu.CategoriesMenuType;
import com.eden.orchid.posts.menu.LatestPostsMenuType;

public class PostsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PostsGenerator.class);

        addToMap(OrchidMenuItemType.class, "postCategories", CategoriesMenuType.class);
        addToMap(OrchidMenuItemType.class, "latestPosts", LatestPostsMenuType.class);

        addToSet(DefaultResourceSource.class, PostsResourceSource.class);
    }
}
