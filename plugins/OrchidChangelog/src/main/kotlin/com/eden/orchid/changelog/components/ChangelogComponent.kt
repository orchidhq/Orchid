package com.eden.orchid.changelog.components

import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.utilities.resolve

@Description("Display the full changelog", name = "Changelog")
class ChangelogComponent : OrchidComponent("changelog", 110) {

    val model: ChangelogModel by lazy {
        context.resolve<ChangelogModel>()
    }

}
