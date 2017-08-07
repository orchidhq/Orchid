package com.eden.orchid.posts;

import com.eden.orchid.OrchidModule;
import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItemFactory;
import com.eden.orchid.posts.menu.CategoriesMenuType;
import com.eden.orchid.posts.menu.LatestPostsMenuType;
import com.eden.orchid.posts.menu.TagsMenuType;

public class PostsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PostsGenerator.class);

        addToMap(OrchidMenuItemFactory.class, "postCategories", CategoriesMenuType.class);
        addToMap(OrchidMenuItemFactory.class, "latestPosts", LatestPostsMenuType.class);
        addToMap(OrchidMenuItemFactory.class, "postTags", TagsMenuType.class);

        addToSet(DefaultResourceSource.class, PostsResourceSource.class);
    }
}
