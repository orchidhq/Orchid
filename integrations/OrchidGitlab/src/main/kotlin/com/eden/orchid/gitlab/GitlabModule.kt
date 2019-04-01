package com.eden.orchid.gitlab

import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.gitlab.publication.GitlabPagesPublisher
import com.eden.orchid.gitlab.wiki.GitlabWikiAdapter
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.wiki.adapter.WikiAdapter

class GitlabModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidPublisher, GitlabPagesPublisher>()
        addToSet<WikiAdapter, GitlabWikiAdapter>()
    }

}

