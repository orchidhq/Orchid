package com.eden.orchid.posts

import com.eden.orchid.api.compilers.TemplateFunction
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.api.theme.permalinks.PermalinkPathType
import com.eden.orchid.posts.components.DisqusComponent
import com.eden.orchid.posts.components.RecentPostsComponent
import com.eden.orchid.posts.functions.ExcerptFunction
import com.eden.orchid.posts.functions.RecentPostsFunction
import com.eden.orchid.posts.menu.LatestPostsMenuType
import com.eden.orchid.posts.permalink.pathtypes.AuthorNamePathType
import com.eden.orchid.posts.permalink.pathtypes.CategoryPathType
import com.eden.orchid.posts.permalink.pathtypes.DayPathType
import com.eden.orchid.posts.permalink.pathtypes.MonthNamePathType
import com.eden.orchid.posts.permalink.pathtypes.MonthPathType
import com.eden.orchid.posts.permalink.pathtypes.SlugPathType
import com.eden.orchid.posts.permalink.pathtypes.YearPathType
import com.eden.orchid.utilities.addToSet

class PostsModule : OrchidModule() {

    override fun configure() {
        withResources(20)

        addToSet<OrchidMenuFactory, LatestPostsMenuType>()
        addToSet<TemplateFunction>(
                ExcerptFunction::class,
                RecentPostsFunction::class)
        addToSet<OrchidGenerator<*>>(
                PostsGenerator::class,
                FeedsGenerator::class)
        addToSet<OrchidComponent>(
                RecentPostsComponent::class,
                DisqusComponent::class)
        addToSet<PermalinkPathType>(
                CategoryPathType::class,
                DayPathType::class,
                MonthNamePathType::class,
                MonthPathType::class,
                SlugPathType::class,
                YearPathType::class,
                AuthorNamePathType::class)
    }
}

