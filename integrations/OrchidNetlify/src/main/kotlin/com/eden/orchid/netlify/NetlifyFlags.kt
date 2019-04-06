package com.eden.orchid.netlify

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class NetlifyFlags : OrchidFlag() {

    @Option @Protected
    @StringDefault("")
    @Description("Your Netlify Personal Access Token.")
    lateinit var netlifyToken: String

}
