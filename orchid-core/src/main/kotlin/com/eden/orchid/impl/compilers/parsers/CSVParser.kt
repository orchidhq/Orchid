package com.eden.orchid.impl.compilers.parsers

import com.eden.orchid.api.compilers.OrchidParser
import com.eden.orchid.api.options.annotations.Archetype
import com.eden.orchid.api.options.archetypes.ConfigArchetype
import com.univocity.parsers.csv.CsvParser
import com.univocity.parsers.csv.CsvParserSettings
import com.univocity.parsers.tsv.TsvParser
import com.univocity.parsers.tsv.TsvParserSettings
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets
import java.util.ArrayList
import java.util.HashMap
import javax.inject.Inject

@Archetype(value = ConfigArchetype::class, key = "services.parsers.csv")
class CSVParser @Inject
constructor() : OrchidParser() {

    override fun getSourceExtensions(): Array<String> {
        return arrayOf("csv", "tsv")
    }

    override fun parse(extension: String, input: String): Map<String, Any> {
        val allRows: List<Array<String>>

        if (extension.equals("csv", ignoreCase = true)) {
            val settings = CsvParserSettings()
            settings.format.setLineSeparator("\n")
            val parser = CsvParser(settings)

            allRows = parser.parseAll(IOUtils.toInputStream(input, StandardCharsets.UTF_8))
        } else {
            val settings = TsvParserSettings()
            settings.format.setLineSeparator("\n")
            val parser = TsvParser(settings)

            allRows = parser.parseAll(IOUtils.toInputStream(input, StandardCharsets.UTF_8))
        }

        val array = ArrayList<Map<String, Any>>()

        val cols = allRows[0]

        for (i in 1 until allRows.size) {
            val row = HashMap<String, Any>()

            for (j in cols.indices) {
                row[cols[j]] = allRows[i][j]
            }

            array.add(row)
        }

        val rows = HashMap<String, Any>()
        rows[OrchidParser.arrayAsObjectKey] = array
        return rows
    }

    override fun serialize(extension: String, input: Any): String {
        return ""
    }
}
