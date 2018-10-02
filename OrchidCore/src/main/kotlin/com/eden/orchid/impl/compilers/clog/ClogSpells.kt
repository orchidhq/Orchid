package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.parseltongue.Spell
import com.eden.orchid.Orchid

import java.net.MalformedURLException
import java.net.URL

object ClogSpells {

    @Spell
    fun baseUrl(`object`: Any): String {
        return Orchid.getInstance().context.site.baseUrl
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlScheme(`object`: Any): String {
        return URL(ClogSpells.baseUrl(`object`)).protocol
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlHost(`object`: Any): String {
        return URL(ClogSpells.baseUrl(`object`)).host
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlPort(`object`: Any): Int {
        return URL(ClogSpells.baseUrl(`object`)).port
    }

    @Spell
    @Throws(MalformedURLException::class)
    fun baseUrlRoot(`object`: Any): String {
        val url = URL(ClogSpells.baseUrl(`object`))

        var urlRoot = ""
        urlRoot += url.protocol + "://"
        urlRoot += url.host

        if (url.port != -1 && url.port != 80) {
            urlRoot += ":" + url.port
        }

        return urlRoot
    }

    @Spell
    fun orchidVersion(`object`: Any): String {
        return Orchid.getInstance().context.site.orchidVersion
    }

    @Spell
    fun env(`object`: Any): String {
        return Orchid.getInstance().context.site.environment
    }

    @Spell
    fun version(`object`: Any): String {
        return Orchid.getInstance().context.site.version
    }

}
