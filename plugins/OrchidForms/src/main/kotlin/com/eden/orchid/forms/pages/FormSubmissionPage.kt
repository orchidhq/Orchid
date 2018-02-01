package com.eden.orchid.forms.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage

@Archetype(value = ConfigArchetype::class, key = "formSubmissionPages")
class FormSubmissionPage(resource: OrchidResource, key: String, title: String?, path: String?)
    : OrchidPage(resource, key, title, path)
