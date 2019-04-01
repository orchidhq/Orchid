package com.eden.orchid.gitlab.wiki

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import javax.inject.Inject

class GitlabWikiAdapter
@Inject
constructor(
    val context: OrchidContext
) : WikiAdapter {

    override fun getType(): String = "gitlab"

    override fun loadWikiPages(section: WikiSection): WikiSection? {
        throw NotImplementedError()
    }

}
