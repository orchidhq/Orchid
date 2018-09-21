package com.eden.orchid.changelog.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.model.ChangelogModel
import javax.inject.Inject

@Description("Display the full changelog", name = "Changelog")
class ChangelogComponent
@Inject
constructor(
        context: OrchidContext,
        val model: ChangelogModel
) : OrchidComponent(context, "changelog", 110)
