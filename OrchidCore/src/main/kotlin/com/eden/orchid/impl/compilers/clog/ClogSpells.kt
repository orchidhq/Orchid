package com.eden.orchid.impl.compilers.clog

import com.caseyjbrooks.clog.parseltongue.Spell
import com.eden.orchid.Orchid

import java.net.MalformedURLException
import java.net.URL

@Suppress("UNUSED_PARAMETER")
class ClogSpells {

    companion object {

        @Spell
        @JvmStatic
        fun baseUrl(`object`: Any): String {
            return Orchid.getInstance().context.site.baseUrl
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlScheme(`object`: Any): String {
            return URL(ClogSpells.baseUrl(`object`)).protocol
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlHost(`object`: Any): String {
            return URL(ClogSpells.baseUrl(`object`)).host
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlPort(`object`: Any): Int {
            return URL(ClogSpells.baseUrl(`object`)).port
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
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
        @JvmStatic
        fun orchidVersion(`object`: Any): String {
            return Orchid.getInstance().context.site.orchidVersion
        }

        @Spell
        @JvmStatic
        fun env(`object`: Any): String {
            return Orchid.getInstance().context.site.environment
        }

        @Spell
        @JvmStatic
        fun version(`object`: Any): String {
            return Orchid.getInstance().context.site.version
        }
    }
}
