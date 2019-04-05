package com.eden.orchid.github

import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.github.publication.GithubPagesPublisher
import com.eden.orchid.github.publication.GithubReleasesPublisher
import com.eden.orchid.utilities.addToSet

class GithubModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidPublisher>(
            GithubPagesPublisher::class,
            GithubReleasesPublisher::class)
//        addToSet<WikiAdapter, GithubWikiAdapter>()
    }

}

