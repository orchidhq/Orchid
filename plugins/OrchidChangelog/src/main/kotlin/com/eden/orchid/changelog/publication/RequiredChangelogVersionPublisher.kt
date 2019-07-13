package com.eden.orchid.changelog.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.changelog.model.ChangelogModel
import com.eden.orchid.utilities.resolve

@Description(
    value = "Checks that a changelog entry has been provided for the current version.",
    name = "Require Changelog"
)
class RequiredChangelogVersionPublisher : OrchidPublisher("requireChangelogVersion", 100) {

    override fun validate(context: OrchidContext): Boolean {
        var valid = super.validate(context)
        val model: ChangelogModel = context.resolve()

        // make sure the current site version has a matching changelog version
        if (model.getVersion(context.site.version) == null) {
            Clog.e("Required changelog entry for version '{}' is missing.", context.site.version)
            valid = false
        }

        return valid
    }

    override fun publish(context: OrchidContext) {
        // do nothing, we just want to validate the pipeline
    }

}
