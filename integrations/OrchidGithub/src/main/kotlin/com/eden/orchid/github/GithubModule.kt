package com.eden.orchid.github

import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.theme.menus.OrchidMenuFactory
import com.eden.orchid.github.menu.GithubMenuItem
import com.eden.orchid.github.publication.GithubPagesPublisher
import com.eden.orchid.github.publication.GithubReleasesPublisher
import com.eden.orchid.github.wiki.GithubWikiAdapter
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.wiki.adapter.WikiAdapter

class GithubModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidPublisher>(
            GithubPagesPublisher::class,
            GithubReleasesPublisher::class)
        addToSet<WikiAdapter, GithubWikiAdapter>()
        addToSet<OrchidMenuFactory, GithubMenuItem>()
    }

}

