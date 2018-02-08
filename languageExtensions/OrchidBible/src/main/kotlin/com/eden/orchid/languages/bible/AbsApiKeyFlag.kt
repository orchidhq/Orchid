package com.eden.orchid.languages.bible

import com.eden.Eden
import com.eden.orchid.api.options.OrchidFlag
import com.eden.orchid.api.options.annotations.Description

@Description("Bible verses can only be looked up when an API key from Bibles.org. Get you key at http://bibles.org/pages/api/signup")
class AbsApiKeyFlag : OrchidFlag("absApiKey", false, null) {

    override fun parseFlag(options: Array<String>): Any {
        val eden = Eden.getInstance()
        eden.config().putString("ABS_ApiKey", options[1])
        eden.config().putString("com.eden.americanbiblesociety.ABSRepository_selectedBibleId", "eng-NASB")
        return options[1]
    }

}
