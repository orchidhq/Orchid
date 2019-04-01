package com.eden.orchid.azure

import com.eden.orchid.api.registration.OrchidModule
import com.eden.orchid.azure.wiki.AzureWikiAdapter
import com.eden.orchid.utilities.addToSet
import com.eden.orchid.wiki.adapter.WikiAdapter

class AzureModule : OrchidModule() {

    override fun configure() {
        addToSet<WikiAdapter, AzureWikiAdapter>()
    }

}

