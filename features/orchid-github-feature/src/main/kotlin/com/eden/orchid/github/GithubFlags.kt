package com.eden.orchid.github

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.EnvironmentVariableAliases
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class GithubFlags : OrchidFlag() {

    @Option
    @Protected
    @StringDefault("")
    @Description("Your Github Personal Access Token.")
    @EnvironmentVariableAliases("GITHUB_TOKEN")
    lateinit var githubToken: String
}
