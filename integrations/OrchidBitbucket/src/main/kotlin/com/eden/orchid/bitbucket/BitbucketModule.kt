package com.eden.orchid.bitbucket

import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.bitbucket.publication.BitbucketCloudPublisher
import com.eden.orchid.utilities.addToSet

class BitbucketModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidPublisher, BitbucketCloudPublisher>()
//        addToSet<WikiAdapter, BitbucketWikiAdapter>()
    }

}

