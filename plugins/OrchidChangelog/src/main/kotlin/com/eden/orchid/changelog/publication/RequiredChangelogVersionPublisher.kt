package com.eden.orchid.changelog.publication

import com.caseyjbrooks.clog.Clog
import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.publication.OrchidPublisher
import com.eden.orchid.changelog.model.ChangelogModel

import javax.inject.Inject

class RequiredChangelogVersionPublisher
@Inject
constructor(
        context: OrchidContext,
        val model: ChangelogModel
) : OrchidPublisher(context, "requireChangelogVersion", 100) {

    override fun validate(): Boolean {
        var valid = super.validate()

        // make sure the current site version has a matching changelog version
        if(model.getVersion(context.site.version) == null) {
            Clog.e("Required changelog entry for version '{}' is missing.", context.site.version)
            valid = false
        }

        return valid
    }

    override fun publish() {
        // do nothing, we just want to validate the pipeline
    }

}
