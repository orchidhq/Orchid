package com.eden.orchid.languages.bible

import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class AbsApiKeyFlag : OrchidFlag() {

    @Option @StringDefault("")
    @Description("Bible verses can only be looked up when an API key from Bibles.org. Get your key at " +
            "https://bibles.org/pages/api/signup"
    )
    @Protected
    lateinit var absApiKey: String

    @Option @StringDefault("eng-NASB")
    @Description("The default version code to use for looking up Bible passages. Version codes can be found at " +
            "https://bibles.org/versions_api by clicking on your desired version, the code is the last URL segment."
    )
    lateinit var absDefaultVersion: String

}
