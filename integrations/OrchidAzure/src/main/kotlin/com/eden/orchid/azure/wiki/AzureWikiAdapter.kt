package com.eden.orchid.azure.wiki

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.wiki.adapter.WikiAdapter
import com.eden.orchid.wiki.model.WikiSection
import javax.inject.Inject

class AzureWikiAdapter
@Inject
constructor(
    val context: OrchidContext
) : WikiAdapter {

    override fun getType(): String = "azure"

    override fun loadWikiPages(section: WikiSection): WikiSection? {
        throw NotImplementedError()
    }

}
