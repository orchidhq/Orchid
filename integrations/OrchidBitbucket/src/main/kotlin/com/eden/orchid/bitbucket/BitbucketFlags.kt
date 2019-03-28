package com.eden.orchid.bitbucket

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.StringDefault

class BitbucketFlags : OrchidFlag() {

    @Option
    @StringDefault("")
    @Description("Your Bitbucket Personal Access Token.")
    lateinit var bitbucketToken: String

}
