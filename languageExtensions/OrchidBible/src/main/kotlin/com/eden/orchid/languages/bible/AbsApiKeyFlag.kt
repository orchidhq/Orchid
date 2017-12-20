package com.eden.orchid.languages.bible

import com.eden.Eden
import com.eden.orchid.api.options.OrchidFlag

class AbsApiKeyFlag : OrchidFlag {

    override fun getFlag(): String {
        return "absApiKey"
    }

    override fun getDescription(): String {
        return "Bible verses can only be looked up when an API key from Bibles.org. Get you key at http://bibles.org/pages/api/signup"
    }

    override fun parseFlag(options: Array<String>): Any {
        val eden = Eden.getInstance()
        eden.config().putString("ABS_ApiKey", options[1])
        eden.config().putString("com.eden.americanbiblesociety.ABSRepository_selectedBibleId", "eng-NASB")
        return options[1]
    }
}
