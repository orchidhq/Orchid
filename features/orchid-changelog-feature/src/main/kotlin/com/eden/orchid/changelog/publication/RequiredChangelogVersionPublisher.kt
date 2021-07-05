package com.eden.orchid.changelog.publication

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.ValidationError
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.validateNotNull
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.utilities.resolve

@Description(
    value = "Checks that a changelog entry has been provided for the current version.",
    name = "Require Changelog"
)
class RequiredChangelogVersionPublisher : OrchidPublisher("requireChangelogVersion") {

    override fun validate(context: OrchidContext): List<ValidationError> {
        val model: ChangelogModel = context.resolve()
        return super.validate(context) + listOfNotNull(
            validateNotNull(
                "context.site.version",
                model.getVersion(context.site.version),
                message = "Required changelog entry for version '${context.site.version}' is missing."
            )
        )
    }

    override fun publish(context: OrchidContext) {
        // do nothing, we just want to validate the pipeline
    }
}
