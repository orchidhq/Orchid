package com.eden.orchid.changelog.components

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.theme.components.OrchidComponent
import com.eden.orchid.changelog.model.ChangelogModel
import javax.inject.Inject

class ChangelogVersionPicker @Inject
constructor(context: OrchidContext, val model: ChangelogModel) : OrchidComponent(context, "changelogVersionPicker", 100)

