package com.eden.orchid.search.pages

import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.eden.orchid.api.resources.resource.OrchidResource
import com.eden.orchid.api.theme.pages.OrchidPage
import com.eden.orchid.search.FormsGenerator

@Archetype(value = ConfigArchetype::class, key = "${FormsGenerator.GENERATOR_KEY}.submissionPages")
@Description(value = "The target parge to redirect to after submitting a form.", name = "Form Submission")
class FormSubmissionPage(
        resource: OrchidResource,
        key: String,
        title: String?
) : OrchidPage(resource, key, title)
