package com.eden.orchid.gitlab

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class GitlabFlags : OrchidFlag() {

    @Option
    @Protected
    @StringDefault("")
    @Description("Your GitLab Personal Access Token.")
    lateinit var gitlabToken: String
}
