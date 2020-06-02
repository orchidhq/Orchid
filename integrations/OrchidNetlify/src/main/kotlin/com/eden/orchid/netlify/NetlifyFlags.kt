package com.eden.orchid.netlify

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class NetlifyFlags : OrchidFlag() {

    @Option
    @Protected
    @StringDefault("")
    @Description("Your Netlify Personal Access Token.")
    lateinit var netlifyToken: String

// Environment variable flags set on Netlify's CI build platform
//----------------------------------------------------------------------------------------------------------------------

    @Option
    @JvmField
    var NETLIFY: Boolean = false

    @Option
    lateinit var CONTEXT: String

    @Option
    @JvmField
    var PULL_REQUEST: Boolean = false

    @Option
    lateinit var DEPLOY_PRIME_URL: String

    @Option
    lateinit var DEPLOY_URL: String

    @Option
    lateinit var URL: String
}
