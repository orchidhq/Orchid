package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.univocity.parsers.csv.CsvParser
import com.univocity.parsers.csv.CsvParserSettings
import com.univocity.parsers.tsv.TsvParser
import com.univocity.parsers.tsv.TsvParserSettings
import org.apache.commons.io.IOUtils

import javax.inject.Inject
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap

class CSVParser @Inject
constructor() : OrchidParser(100) {

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("csv", "tsv")
    }

    override fun parse(extension: String, input: String): Map<String, Any> {
        val allRows: List<Array<String>>

        if (extension.equals("csv", ignoreCase = true)) {
            val settings = CsvParserSettings()
            settings.format.setLineSeparator("\n")
            val parser = CsvParser(settings)

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.forName("UTF-8")))
        } else {
            val settings = TsvParserSettings()
            settings.format.setLineSeparator("\n")
            val parser = TsvParser(settings)

            allRows = parser.parseAll(IOUtils.toInputStream(input, Charset.forName("UTF-8")))
        }

        val array = ArrayList<Map<String, Any>>()

        val cols = allRows[0]

        for (i in 1 until allRows.size) {
            val `object` = HashMap<String, Any>()

            for (j in cols.indices) {
                `object`[cols[j]] = allRows[i][j]
            }

            array.add(`object`)
        }

        val `object` = HashMap<String, Any>()
        `object`[OrchidParser.arrayAsObjectKey] = array
        return `object`
    }

    override fun serialize(extension: String, input: Any): String {
        return ""
    }
}
