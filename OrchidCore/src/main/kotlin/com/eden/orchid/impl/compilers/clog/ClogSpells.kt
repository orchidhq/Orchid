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
        fun baseUrl(value: Any): String {
            return Orchid.getInstance().context.site.baseUrl
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlScheme(value: Any): String {
            return URL(ClogSpells.baseUrl(value)).protocol
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlHost(value: Any): String {
            return URL(ClogSpells.baseUrl(value)).host
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlPort(value: Any): Int {
            return URL(ClogSpells.baseUrl(value)).port
        }

        @Spell
        @Throws(MalformedURLException::class)
        @JvmStatic
        fun baseUrlRoot(value: Any): String {
            val url = URL(ClogSpells.baseUrl(value))

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
        fun orchidVersion(value: Any): String {
            return Orchid.getInstance().context.site.orchidVersion
        }

        @Spell
        @JvmStatic
        fun env(value: Any): String {
            return Orchid.getInstance().context.site.environment
        }

        @Spell
        @JvmStatic
        fun version(value: Any): String {
            return Orchid.getInstance().context.site.version
        }
    }
}
