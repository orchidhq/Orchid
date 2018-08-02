package com.eden.orchid.languages.bible

import com.eden.Eden
import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description
import com.eden.orchid.api.options.annotations.Option
import com.eden.orchid.api.options.annotations.Protected
import com.eden.orchid.api.options.annotations.StringDefault

class AbsApiKeyFlag : OrchidFlag() {

    @Option @StringDefault("")
    @Description("Bible verses can only be looked up when an API key from Bibles.org. Get your key at " +
            "http://bibles.org/pages/api/signup"
    )
    @Protected
    lateinit var absApiKey: String

    override fun getParsedFlags(): MutableMap<String, Value> {
        Eden.getInstance().config().putString("ABS_ApiKey", absApiKey)
        Eden.getInstance().config().putString("com.eden.americanbiblesociety.ABSRepository_selectedBibleId", "eng-NASB")

        return super.getParsedFlags()
    }
}
