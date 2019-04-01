package com.eden.orchid.bitbucket.wiki

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import javax.inject.Inject

class BitbucketWikiAdapter
@Inject
constructor(
    val context: OrchidContext
) : WikiAdapter {

    override fun getType(): String = "bitbucket"

    override fun loadWikiPages(section: WikiSection): WikiSection? {
        throw NotImplementedError()
    }

}
