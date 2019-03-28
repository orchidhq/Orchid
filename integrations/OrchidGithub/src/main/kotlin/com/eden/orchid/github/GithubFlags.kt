package com.eden.orchid.github

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

class GithubFlags : OrchidFlag() {

    @Option
    @StringDefault("")
    @Description("Your Github Personal Access Token.")
    lateinit var githubToken: String

}
