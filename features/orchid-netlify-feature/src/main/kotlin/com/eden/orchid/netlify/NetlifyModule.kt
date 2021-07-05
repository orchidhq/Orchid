package com.eden.orchid.netlify

import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.api.site.BaseUrlFactory
import com.eden.orchid.netlify.publication.NetlifyPublisher
import com.eden.orchid.netlify.site.NetlifyBaseUrlFactory
import com.eden.orchid.utilities.addToSet

class NetlifyModule : OrchidModule() {

    override fun configure() {
        addToSet<OrchidPublisher, NetlifyPublisher>()
        addToSet<BaseUrlFactory, NetlifyBaseUrlFactory>()
    }
}
