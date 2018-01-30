package com.eden.orchid.posts

import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.options.OptionExtractor
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.menuItem.OrchidMenuItem
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.components.DisqusComponent
import com.eden.orchid.posts.components.RecentPostsComponent
import com.eden.orchid.posts.menu.LatestPostsMenuType
import com.eden.orchid.posts.permalink.pathTypes.*
import com.eden.orchid.posts.utils.AuthorOptionExtractor

class PostsModule : OrchidModule() {

    override fun configure() {
        addToSet(OrchidGenerator::class.java, PostsGenerator::class.java)

        addToSet(OrchidMenuItem::class.java,
                LatestPostsMenuType::class.java)

        addToSet(OrchidComponent::class.java,
                RecentPostsComponent::class.java,
                DisqusComponent::class.java)

        addToSet(PluginResourceSource::class.java,
                PostsResourceSource::class.java)

        addToSet(PermalinkPathType::class.java,
                CategoryPathType::class.java,
                DayPathType::class.java,
                MonthNamePathType::class.java,
                MonthPathType::class.java,
                SlugPathType::class.java,
                YearPathType::class.java,
                AuthorNamePathType::class.java)

        addToSet(OptionExtractor::class.java,
                AuthorOptionExtractor::class.java)
    }
}

