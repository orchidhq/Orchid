package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.parseltongue.Spell
import com.eden.orchid.api.OrchidContext
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNUSED_PARAMETER")
class ClogSpells
@Inject
constructor(
    val contextProvider: Provider<OrchidContext>
) {

    @Spell
    fun baseUrl(value: Any): String {
        return contextProvider.get().site.baseUrl
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlScheme(value: Any): String {
        return URL(baseUrl(value)).protocol
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlHost(value: Any): String {
        return URL(baseUrl(value)).host
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlPort(value: Any): Int {
        return URL(baseUrl(value)).port
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlRoot(value: Any): String {
        val url = URL(baseUrl(value))

        var urlRoot = ""
        urlRoot += url.protocol + "://"
        urlRoot += url.host

        if (url.port != -1 && url.port != 80) {
            urlRoot += ":" + url.port
        }

        return urlRoot
    }

    @Spell
    fun orchidVersion(value: Any): String {
        return contextProvider.get().site.orchidVersion
    }

    @Spell
    fun env(value: Any): String {
        return contextProvider.get().site.environment
    }

    @Spell
    fun version(value: Any): String {
        return contextProvider.get().site.version
    }

}
