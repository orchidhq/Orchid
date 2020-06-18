package com.eden.orchid.netlify.site

import com.eden.orchid.api.OrchidContext
import com.eden.orchid.api.site.BaseUrlFactory
import com.google.inject.name.Named
import javax.inject.Inject

class NetlifyBaseUrlFactory
@Inject constructor(
    @Named("NETLIFY")          private val isRunningOnNetlifyCi: Boolean,
    @Named("CONTEXT")          private val context: String,
    @Named("PULL_REQUEST")     private val isPullRequest: Boolean,
    @Named("DEPLOY_PRIME_URL") private val deployPrimeUrl: String,
    @Named("DEPLOY_URL")       private val deployUrl: String,
    @Named("URL")              private val url: String
) : BaseUrlFactory("netlify", 10) {

    private val isBranchDeploy get() = context == "branch-deploy"

    override fun isEnabled(context: OrchidContext): Boolean {
        val isProduction = context.isProduction
        return isProduction && isRunningOnNetlifyCi
    }

    override fun getBaseUrl(context: OrchidContext): String {
        return when {
            isBranchDeploy -> deployPrimeUrl // branch deploys
            isPullRequest -> deployUrl       // PR deploy previews
            else -> url                      // production deploys
        }
    }
}
