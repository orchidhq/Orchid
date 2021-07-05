package com.eden.orchid.changelog.adapter

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.ModularType
import com.eden.orchid.changelog.model.ChangelogVersion

interface ChangelogAdapter : ModularType {

    fun loadChangelogEntries(context: OrchidContext): List<ChangelogVersion>
}
