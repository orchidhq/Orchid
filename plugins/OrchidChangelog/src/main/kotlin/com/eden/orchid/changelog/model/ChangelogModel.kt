package com.eden.orchid.changelog.model

import com.eden.orchid.api.generators.OrchidCollection
import com.eden.orchid.api.generators.OrchidGenerator
import com.eden.orchid.api.theme.pages.OrchidPage
import javax.inject.Singleton

class ChangelogModel(
    val versions: List<ChangelogVersion>
) : OrchidGenerator.Model {

    override val allPages: List<OrchidPage> = emptyList()
    override val collections: List<OrchidCollection<*>> = emptyList()

    fun getVersion(versionName: String): ChangelogVersion? {
        return versions.find { it.version == versionName }
    }
}

