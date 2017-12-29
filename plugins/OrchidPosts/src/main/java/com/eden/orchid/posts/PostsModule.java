package com.eden.orchid.posts;

import com.eden.orchid.api.generators.OrchidGenerator;
import com.eden.orchid.api.options.OptionExtractor;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.theme.components.OrchidComponent;
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem;
import com.eden.orchid.posts.components.DisqusComponent;
import com.eden.orchid.posts.components.PostTagsComponent;
import com.eden.orchid.posts.components.RecentPostsComponent;
import com.eden.orchid.posts.menu.CategoriesMenuType;
import com.eden.orchid.posts.menu.LatestPostsMenuType;
import com.eden.orchid.posts.menu.TagsMenuType;
import com.eden.orchid.posts.permalink.PermalinkPathType;
import com.eden.orchid.posts.permalink.pathTypes.ArchiveIndexPathType;
import com.eden.orchid.posts.permalink.pathTypes.CategoryPathType;
import com.eden.orchid.posts.permalink.pathTypes.DataPropertyPathType;
import com.eden.orchid.posts.permalink.pathTypes.DayPathType;
import com.eden.orchid.posts.permalink.pathTypes.MonthNamePathType;
import com.eden.orchid.posts.permalink.pathTypes.MonthPathType;
import com.eden.orchid.posts.permalink.pathTypes.SlugPathType;
import com.eden.orchid.posts.permalink.pathTypes.TagPathType;
import com.eden.orchid.posts.permalink.pathTypes.TitlePathType;
import com.eden.orchid.posts.permalink.pathTypes.YearPathType;

public class PostsModule extends OrchidModule {

    @Override
    protected void configure() {
        addToSet(OrchidGenerator.class, PostsGenerator.class);

        addToSet(OrchidMenuItem.class,
                CategoriesMenuType.class,
                LatestPostsMenuType.class,
                TagsMenuType.class);

        addToSet(OrchidComponent.class,
                PostTagsComponent.class,
                RecentPostsComponent.class,
                DisqusComponent.class);

        addToSet(PluginResourceSource.class,
                PostsResourceSource.class);

        addToSet(PermalinkPathType.class,
                ArchiveIndexPathType.class,
                CategoryPathType.class,
                DataPropertyPathType.class,
                DayPathType.class,
                MonthNamePathType.class,
                MonthPathType.class,
                SlugPathType.class,
                TagPathType.class,
                TitlePathType.class,
                YearPathType.class);

        addToSet(OptionExtractor.class,
                AuthorOptionExtractor.class);
    }
}
