package com.eden.orchid.languages.impl;

import com.eden.orchid.api.compilers.OrchidParser;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class CSVParser extends OrchidParser {

    @Override
    public String[] getSourceExtensions() {
        return new String[] {"csv", "tsv"};
    }

    @Override
    public JSONObject parse(String extension, String input) {
        List<String[]> allRows;

        if(extension.equalsIgnoreCase("csv")) {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            CsvParser parser = new CsvParser(settings);

            allRows = parser.parseAll(org.apache.commons.io.IOUtils.toInputStream(input));
        }
        else {
            TsvParserSettings settings = new TsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            TsvParser parser = new TsvParser(settings);

            allRows = parser.parseAll(org.apache.commons.io.IOUtils.toInputStream(input));
        }

        JSONArray array = new JSONArray();

        String[] cols = allRows.get(0);

        for (int i = 1; i < allRows.size(); i++) {
            JSONObject object = new JSONObject();

            for (int j = 0; j < cols.length; j++) {
                object.put(cols[j], allRows.get(i)[j]);
            }

            array.put(object);
        }

        JSONObject object = new JSONObject();
        object.put(OrchidParser.arrayAsObjectKey, array);
        return object;
    }
}
